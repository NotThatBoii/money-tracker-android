# Phase 1 verification

Verified locally on Windows on 2026-09-17 with Temurin JDK 17, Gradle 8.13,
Android platform 36 and build-tools 35.0.0.

```text
gradlew.bat :app:assembleDebug :app:testDebugUnitTest :app:lintDebug :app:assembleRelease
BUILD SUCCESSFUL
```

| Check | Result |
| --- | --- |
| Debug APK compilation/packaging | Passed |
| R8-minified, resource-shrunk unsigned release APK | Passed |
| Money parsing, formatting, precision, currency and overflow | 11 tests passed |
| Active account totals, sample arithmetic, currencies and liabilities | 6 tests passed |
| Domain model invariants | 6 tests passed |
| Room native SQLite persistence, rollback, constraints and repository mapping | 8 tests passed |
| ViewModel initialization and safe retry | 2 tests passed |
| Compose navigation, settings return, dark-theme error/retry | 2 tests passed |
| Android lint | No errors or warnings; 18 informational dependency-update hints |
| Exported Room schema differs from committed v1 | No |
| Release APK permissions | No Internet, banking, storage, or credential permissions |

Total: **35 tests passed, 0 failures, 0 skipped**. Room and Compose tests use
Robolectric API 35; database tests use native SQLite. The release contains only an
AndroidX-generated app-scoped signature permission for private dynamic receivers.

The build initially found an unescaped local Windows SDK path and a missing plural
resource; both were fixed before the successful verification above. Dependency-version
notices remain visible as informational hints because upgrades must be evaluated as
coherent toolchain changes. The target-SDK upgrade advisory is also informational:
this foundation explicitly targets API 36, and CI may have newer SDKs installed.
Before distribution, review the current target requirement and test newer platform
behavior as an explicit upgrade. Other lint warnings fail the build.

## Scope and remaining validation

- Tests validate Phase 1 only. Expense/income balance mutations, transfer atomicity
  as a business operation, and analytics exclusion tests arrive with Phase 3/5.
- The Room rollback test proves multi-write rollback on a constraint failure; it
  does not claim that the future transfer feature is implemented.
- No physical device or emulator acceptance run has been performed. Device-level
  visual/accessibility checks and biometric verification remain future work.
- There is no old released database to migrate yet. Future migrations need populated
  upgrade tests; v1 is exported and destructive fallback is prohibited.
- The debug APK is for foundation review. Feature routes are explicit placeholders.
- CI is configured in `.github/workflows/android.yml` for the same build/test/lint
  gates and schema-drift checks. Remote CI status is separate from this local result.

**Next phase:** account CRUD/archive/manual adjustment, balance dashboard, and their
tests. Phases 2–8 have not been started.
