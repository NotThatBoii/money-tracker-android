# Architecture and financial invariants

## Data flow

Compose → ViewModel → domain repository interface → Room repository → DAO → SQLite.
Flows travel back through repositories to lifecycle-aware Compose state collectors.
`AppContainer` owns one Room database for the application's lifetime. No Activity,
ViewModel, or Composable owns the database. Manual dependency injection keeps this
single-module app easy to test without a DI framework.

The Phase 1 shell ViewModel initializes reference data and reports readiness. Actual
feature ViewModels and write use cases are introduced in the appropriate phase.
Repositories currently expose reads only; DAOs are persistence primitives, never
called by the UI. All five domain models are distinct from Room entities.

## Monetary semantics

- Amounts are signed `Long` minor units plus an uppercase ISO currency code.
  PHP 123.45 is 12345. No stored monetary `Float` or `Double` exists.
- Decimal parsing uses `BigDecimal`, rejects ambiguous/exponent/grouped input,
  rejects fractional minor units, and fails on overflow. Formatting also uses
  `BigDecimal`, preserving large amounts down to the final centavo.
- `Money` addition/subtraction rejects different currencies and detects overflow.
  There is no currency exchange implementation or implicit conversion.
- Totals include active accounts only, grouped by currency. Credit debt is a
  negative balance and reduces that currency's total; it is not available credit.
  Product labeling for liabilities will be made explicit in Phase 2.
- Transactions have positive amounts; type determines direction. Transfers require
  a distinct destination and no spending category. Income/expenses require a category.
- Phase 3 must enforce account existence, active state, and matching currency,
  then update both balances and insert one transfer in a **single Room transaction**.
  A transfer must never become two unrelated income/expense rows. Reversal/edit
  operations must follow the same atomicity rule. No balance-write API exists yet.
- Balances are current snapshots. Opening balances and future adjustments must not
  masquerade as earned income; the audit/adjustment mechanism belongs in Phase 2.
- Goals track earmarked progress only. Adding them to account totals would double count money.

## Schema

`Account` has name, institution, type, signed balance, currency, optional last four
digits/icon/color, last-updated instant, and archived state. Bank names are ordinary
user-controlled labels, not integrations. An account can represent any named bank,
e-wallet, cash, or custom institution.

`Transaction` references its source account, optional destination, and optional
category. Both account references and category references use restrictive foreign
keys. Archive accounts; do not cascade-delete financial history. Indexes cover
account/date, destination, category, chronological history, and future provenance.

`Budget` references a category and is unique by category, calendar month, and currency.
`SavingsGoal` stores target/saved amounts in the same currency and an optional epoch-day
target date. Timestamps are UTC epoch milliseconds; budgets use ISO `YearMonth`.
Future reporting should derive ranges using an injected `Clock` and the user's zone,
with half-open `[start, end)` intervals.

## Migrations

Version 1 is the first database, so there is no invented 0→1 migration. Room exports
`app/schemas/.../1.json`; keep every released schema in Git. No destructive fallback
or main-thread database access is enabled. Initialization inserts categories within
`withTransaction` with conflict-ignore, so restart never overwrites existing rows.

For every future schema change:

1. Increment the version and preserve the previous schema JSON.
2. Add an explicit `Migration` to `MoneyTrackerDatabase.MIGRATIONS` (or a reviewed
   Room auto-migration for a demonstrably safe additive change).
3. Test upgrade paths with populated old databases and verify row/value preservation.
4. Commit the new generated schema and pass the build/tests before release.

Room foreign keys/uniqueness protect relationships. Scalar/business invariants are
validated by domain constructors and, in later phases, write repositories/use cases.
Direct DAO writes must not be exposed as a public feature API.

## Security boundaries

- V1 has no Internet permission, bank login UI, WebView, automation, or cloud SDK.
- No fields exist for bank passwords, PINs, OTPs, card numbers, cookies, or tokens.
  Optional account suffixes accept exactly four digits at the domain boundary.
- App-private Room storage is **not application-level encrypted**. Device security
  protects the sandbox; device compromise/root is outside this foundation's protections.
- Backup and device transfer are excluded through the manifest and extraction rules.
  App lock and additional hardening are Phase 7, not a current protection claim.
- No financial logging or SQL query logging. Failures show a generic message and
  never reset the database. No sample balances are inserted during startup.
- Future integration tokens belong behind a dedicated Android Keystore-backed
  encrypted token store; never ordinary preferences or database string fields.

## Future data sources (design boundary only)

Phase 8 will add provider/import contracts beneath the repository layer, independent
of UI and provider JSON formats. They will expose normalized domain data plus source
identity. Only legitimate authorized read-only providers are permitted; bank passwords
must never pass through this app's UI. No fake bank API or network client is implemented.

Transactions already reserve nullable `externalId` and `fingerprint` plus a nonsecret
`sourceKey`. Uniqueness is scoped to account + source. Manual rows leave provenance
IDs null, allowing legitimate identical purchases. Future parsers must version their
normalization/fingerprint algorithms, distinguish repeated legitimate statement rows,
reconcile transfers, and write accepted rows atomically. A raw amount/date hash alone
is not sufficient proof of a duplicate.

## Toolchain references

Pinned versions are intentional and reproducible, not dynamic “latest” ranges.
- [AGP 8.13 compatibility](https://developer.android.com/build/releases/agp-8-13-0-release-notes)
- [Compose BOM](https://developer.android.com/develop/ui/compose/bom)
- [Room releases](https://developer.android.com/jetpack/androidx/releases/room)
