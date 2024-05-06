### System design

#### Terms

- `loaylty points (aka points)` - points that can be earned for purchasing services and spent on additional benefits and
  rewards
- `loyalty tier (aka customer tier)` - defines rewards and benefits available to the user with given loyalty tier
- `points tier validity date` - date till when points are used to calculate the loyalty level
- `points spending validity date` - date till when points can be spent on benefits

#### Modules and responsibilities

The below list of modules describes what are the modules and their responsibilities. This is critical to correctly
allocate implementation of features in the correct module. Modules are responsible for behaviors/features (verbs) and
not entities (nouns). This makes the modules smaller and more cohesive. As a result, changes in features/behaviors
requirements should result in local changes (modification of single exciting module or creation of new module).

- `accumulate-points` - Responsible for recording points earning and spending.
  It publishes current-points balance and their validity dates after each change.
  It's also providing points history.
- `evaluate-booking-points` - Evaluates each booking to determine eligibility and point accrual based on customer's
  current tier and booking specifics. It interacts with `retrieve-customer-tier` to fetch the appropriate loyalty level
  and it calls `accumulate-points`.
- `retrieve-customer-tier` - manages configuration of loyalty tires and maintains customer tier for each customer based
  on current points balance published by `accumulate-spend-points` and potentially by other activities recorded
  by `track-customer-activity`
- `manage-rewards` - Manages the configuration and availability of various reward options. It uses data
  from `retrieve-customer-tier` to tailor reward offerings to individual customer tiers.
- `redeem-points` - Allows customers to redeem their accumulated points for rewards. This module works closely
  with `manage-rewards` to ensure redemption options are updated and aligned with current offerings.
- `offer-exclusive-deals` - Provides VIP customers with exclusive offers and early access to promotions, depending on
  their loyalty status, which is determined by `retrieve-customer-tier`.
- `track-customer-activity` - Monitors customer booking activities and spending to support tier upgrades or downgrades.
  It supplies necessary data to `retrieve-customer-tier` and `generate-insights`.
- `generate-insights` - Analyzes data from customer interactions to provide analytics on loyalty program performance,
  including tier distribution and impact on bookings. This module depends on data from `track-customer-activity`
  and `accumulate-points`.
- `send-targeted-promotions` - Uses customer data and loyalty status to send personalized marketing communications, such
  as email or SMS. It depends on data from `track-customer-activity` and `retrieve-customer-tier`.


```mermaid
graph TD;
    evaluate-booking-points[Evaluate Booking Points] -->|calls| accumulate-points[Accumulate Points]
    evaluate-booking-points -->|fetches tier from| retrieve-customer-tier[Retrieve Customer Tier]

    retrieve-customer-tier -->|uses points from| accumulate-points
    retrieve-customer-tier -->|uses activity data from| track-customer-activity[Track Customer Activity]
    
    manage-rewards[Manage Rewards] -->|uses tier info from| retrieve-customer-tier
    
    redeem-points[Redeem Points] -->|coordinates with| manage-rewards

    offer-exclusive-deals[Offer Exclusive Deals] -->|determines offers based on tier| retrieve-customer-tier
    
    track-customer-activity -->|supplies data to| retrieve-customer-tier
    track-customer-activity -->|provides activity data to| generate-insights[Generate Insights]
    
    generate-insights -->|uses points data from| accumulate-points

    send-targeted-promotions[Send Targeted Promotions] -->|uses data from| track-customer-activity
    send-targeted-promotions -->|uses tier info from| retrieve-customer-tier
```

TODO describe how to connect accumulation and spending of points when points are valid in time?
