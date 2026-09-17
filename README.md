# Money Tracker Android

An offline, native Android personal finance app for Philippine bank accounts,
e-wallets, and cash. **Phase 1 is the working foundation, not the complete finance app.**
There are no bank connections, credential forms, scraping, or network permissions.

## Phase 1

- Kotlin, Compose, Material 3, Navigation Compose, coroutines, Flow/StateFlow, MVVM.
- Light/dark themes follow the system; bottom navigation becomes a rail at 600 dp.
- Functional Home, Transactions, Accounts, Analytics, and Settings navigation shell.
- Room v1: accounts, transactions, categories, monthly budgets, savings goals;
  foreign keys, indexes, exported schema, and explicit migration registration.
- Domain models, repository interfaces, Room implementations, application-scoped
  dependency container, and lifecycle-aware ViewModel state collection.
- Exact `Long` minor units, checked arithmetic, strict decimal parsing, currency
  formatting, and active-account totals grouped by currency.
- Idempotent initialization adds 15 reference categories only. No sample money.
- Safe loading/error/retry states. Feature screens clearly identify their planned phase.

## Build

Use JDK 17 and an Android SDK with platform 36 and build-tools 35.0.0.
Open the repository directory in Android Studio, or set `ANDROID_HOME` / an
untracked `local.properties` containing `sdk.dir=/path/to/android-sdk`.
The Gradle wrapper downloads the pinned Gradle distribution and verifies its SHA-256.

```sh
./gradlew :app:assembleDebug :app:testDebugUnitTest :app:lintDebug :app:assembleRelease
```

Windows: use `gradlew.bat`. Minimum supported Android version: 8.0 / API 26.
The debug APK is `app/build/outputs/apk/debug/app-debug.apk`. The release build
is deliberately unsigned; no production signing keys belong in this repository.
Unit, Room/SQLite, and Compose navigation tests run through Robolectric without an emulator.

## Structure

```text
app/src/main/java/com/moneytracker/android/
  data/local/       Room database, entities and DAOs
  data/repository/  Entity/domain mapping and Room repository implementations
  domain/model/    Android-independent models and money invariants
  domain/repository/ Repository contracts
  domain/usecase/  Active-account totals
  di/             Application-scoped dependency composition
  ui/             Shell ViewModel, feature screens, navigation, components, theme
  util/           Currency formatting
app/schemas/       Committed Room schema history
app/src/test/      Domain, persistence, ViewModel, and Compose tests
docs/              Architecture, security boundaries, phase verification
```

## Next phases

2. Manual account creation/edit/archive/adjustment and the real balance dashboard.
3. Income, expenses, and **atomic** same-currency transfers; sample data only on explicit debug action.
4. Searchable/date-grouped history, filters, and category presentation.
5. Analytics and charts excluding transfers from income and expenses.
6. Category budgets and savings goals.
7. Biometric/device-credential app lock and security hardening.
8. Read-only provider contracts and statement import/normalization/deduplication pipeline.

Each phase must build and pass its relevant tests before the next begins.
See [architecture](docs/ARCHITECTURE.md) for the invariants later phases must preserve.
