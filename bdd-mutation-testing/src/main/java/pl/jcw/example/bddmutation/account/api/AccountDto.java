package pl.jcw.example.bddmutation.account.api;

import java.util.UUID;

public record AccountDto(UUID accountId, UUID ownerId, String accountName) {}
