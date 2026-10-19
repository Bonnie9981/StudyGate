# StudyGate Android（MVP）

StudyGate 是一個 Kotlin 原生 Android 專案原型：使用者選擇要限制的 App；當 Accessibility Service 偵測到目標 App 成為前景時，嘗試開啟答題頁；答對原題並完成補救題後，該 App 解鎖 10 分鐘。

## 開發環境
- Android Studio（建議近期穩定版）
- JDK 17
- Android SDK Platform 35
- Gradle Wrapper 可在 Android Studio 首次匯入時由 IDE 建立；本原始碼未附 wrapper JAR。

## 建置
1. 解壓縮 `StudyGate.zip`。
2. 用 Android Studio 選擇 **Open**，開啟 `StudyGate` 資料夾。
3. 等待 Gradle 同步；若 IDE 提示建立/更新 Gradle Wrapper，依照 IDE 指示操作。
4. 連接 Android 手機，啟用開發人員選項與 USB 偵錯，按 Run 安裝。
5. 開啟 StudyGate，點「開啟 Android 無障礙設定」，在系統設定中手動啟用「StudyGate App 攔截服務」。
6. 回到 StudyGate，勾選要限制的 App，點「儲存限制 App」。
7. 測試前先用非重要 App；若系統不允許背景啟動 Activity，攔截頁可能無法彈出。

## 權限與隱私
- 不需要網路權限、帳號或雲端服務。
- 透過 Android Accessibility Service 接收視窗狀態事件；使用者必須在系統設定中自行啟用。
- 本版本未讀取視窗文字或執行點擊手勢。
- Manifest 使用 `<queries>` 查詢具有 Launcher 入口的 App；未宣告 `QUERY_ALL_PACKAGES`。
- 無障礙服務屬高敏感權限，需向使用者清楚說明用途。若上架 Google Play，必須遵守當前 Accessibility API 政策與申報要求；不能把它當作隱藏或強制控制使用者的工具。

## 已知限制（重要）
1. **不是系統級硬鎖**：Android 沒有提供一般第三方 App 無條件封鎖其他 App 的通用 API。這裡是偵測前景變化後嘗試顯示答題 Activity，可能有短暫閃現、延遲或被系統阻止。
2. OEM / Android 版本差異：背景 Activity 啟動限制、省電策略、無障礙服務被回收、分割畫面、通知啟動、最近使用 App 等都可能造成繞過或失效。
3. 使用者可在系統設定停用無障礙權限、強制停止或解除安裝本 App；本版本不提供防卸載或家長管理級封鎖。
4. 目前題庫是內建示範題，並非完整或經教師審核的學測題庫；需要校對與擴充。題型目前為選擇題，未實作自由輸入答案、AI 出題、錯題紀錄或雲端同步。
5. 此專案未附 Gradle Wrapper JAR，也未在此環境完成 Android SDK 編譯或真機測試。請在 Android Studio 中同步、編譯並於目標手機驗證。

## 下一步建議
- 修正並測試補救題流程與題庫答案。
- 使用 Room 保存答題紀錄、錯題本與每個 App 的獨立解鎖期限。
- 新增輸入數值、公式題，隨機參數與單位檢查。
- 加入每日目標、使用統計及明確的緊急退出方式。
