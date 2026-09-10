# ROX FOLLOW Native Migration

Architecture:
- Native Android
- Kotlin
- Jetpack Compose
- No Capacitor application shell
- No React WebView application shell

Original project:
../project

Package:
com.roxfollow.app

Migration rules:
- Existing screens must be preserved
- Existing visual design must be preserved
- Existing fonts must not be changed unnecessarily
- Existing Admin behavior must remain unchanged
- Existing Firebase behavior must be preserved
- Existing 3 Adsterra banner placements must be preserved where technically supported
- Unity Rewarded Ads will use native Android SDK
- Fake rewarded-ad fallback must not return coins
