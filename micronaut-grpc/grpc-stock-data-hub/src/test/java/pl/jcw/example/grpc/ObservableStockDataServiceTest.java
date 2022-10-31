package pl.jcw.example.grpc;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import pl.jcw.example.grpc.News.NewsCategory;
import pl.jcw.example.grpc.ObservableStockDataService.StockDataObserver;
import pl.jcw.example.grpc.SymbolData.Builder;
import pl.jcw.example.grpc.SymbolDataRequest.DataType;

class ObservableStockDataServiceTest {

  private static final String SYMBOL_ID = "test_symbol";
  ObservableStockDataService service = new ObservableStockDataService();

  @Test
  void shouldReceiveUpdatesWhenRegisteredAsObserverOfAllUpdates() {
    // given
    List<SymbolData> receivedUpdates = new ArrayList<>();
    SymbolData news = news();
    SymbolData quotation = quotation();
    SymbolData companyDetails = companyDetails();

    // when
    StockDataObserver observer = service.createObserver(receivedUpdates::add);
    observer.subscribe(
        SymbolDataRequest.newBuilder().setSymbolId(SYMBOL_ID).addTypes(DataType.ALL).build());
    service.publish(news);
    service.publish(quotation);
    service.publish(companyDetails);

    // then
    assertThat(receivedUpdates).containsExactlyInAnyOrder(news, quotation, companyDetails);
  }

  @Test
  void shouldReceiveOnlyNewsUpdates() {
    // given
    List<SymbolData> receivedUpdates = new ArrayList<>();
    SymbolData news = news();

    // when
    StockDataObserver observer = service.createObserver(receivedUpdates::add);
    observer.subscribe(
        SymbolDataRequest.newBuilder().setSymbolId(SYMBOL_ID).addTypes(DataType.NEWS).build());
    service.publish(news);
    service.publish(quotation());
    service.publish(companyDetails());

    // then
    assertThat(receivedUpdates).containsExactly(news);
  }

  @Test
  void shouldStreamCompanyData() {
    // given
    SymbolData companyDetails = companyDetails();

    // when
    List<CompanyDetails> before = service.streamAllCompaniesDetails().toList();
    service.publish(companyDetails);
    List<CompanyDetails> after = service.streamAllCompaniesDetails().toList();

    // then
    assertThat(before).isEmpty();
    assertThat(after).containsExactly(companyDetails.getDetails());
  }

  private SymbolData companyDetails() {
    return dataBuilder().setDetails(CompanyDetails.newBuilder().setName("test company")).build();
  }

  private SymbolData news() {
    return dataBuilder()
        .setNews(News.newBuilder().setMessage("All is good!").setCategory(NewsCategory.POSITIVE))
        .build();
  }

  private SymbolData quotation() {
    return dataBuilder().setQuotation(Quotation.newBuilder().setBid(1).setAsk(2)).build();
  }

  private Builder dataBuilder() {
    return SymbolData.newBuilder().setTimestamp(System.currentTimeMillis()).setSymbolId(SYMBOL_ID);
  }
}
