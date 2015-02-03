# kajiradoubra 可程式遙控震動胸罩
=================================

## 自我審查 十八禁 Adults only.

Please don't scroll down if you are under age of 18.

如果您未滿十八歲, 請勿繼續往下捲動.

![Adults only US](http://upload.wikimedia.org/wikipedia/commons/b/be/ESRB_2013_Adults_Only.png)
![Adults only TW](http://upload.wikimedia.org/wikipedia/commons/d/d5/GSRR_R_logo.svg)

Please close the window immediately if you are under age of 18.

如果您未滿十八歲, 請立刻關閉視窗.

# 哼哼 向東瀛女優國致敬

## 說明

本計畫為一可穿戴式設備. 透過手機藍芽遙控設備震動.

設備端為胸罩, 外加兩個小型震動馬達. 使用 Arduino 控制.
Arduino 連接藍芽模組. 並用 motor driver 控制震動馬達.

手機為 Android 手機, 安裝我們做的 app, 透過藍芽來遙控 Arduino.

## 玩法

此產品提供多種有趣玩法. 可自行 DIY 更多玩法. 歡迎分享 idea.

### 胸部按摩

本產品總共提供 36 段強度控制. 透過手機 app
介面可以任意調整自己覺得最舒服的強度.
每天按摩胸部 5 分鐘聽說還可以變大.
多愛自己一點多多按摩.

### 變頻震動

如果覺得單一強度震動太無聊. 目前手機 app 提供多種變頻震動.
列舉如下:
 * FF 水晶震動: 從左到右, 再由右到左, 讓您回味無窮
 * 愛的鼓勵: 給自己一個愛的鼓勵
 * ...

### 摩斯電碼

朋友可以用手機送摩斯電碼給您. 您不用用耳朵接收. 用胸部接收就可以了.

另外也可以拿來訓練自己摩斯電碼的解碼能力. 業餘無線電人員二級執照會考.

### 人偶控制

> 在人偶的製作材料中加入活體，該人偶就會被稱為禁忌人偶


### Programmer mode

自己編寫 code. 創造自己的震動模式. 控制碼如下:

 * l{0-9A-Z}: 左馬達 36 段強度控制. l0 為停止震動. lZ 為最強震動
 * r{0-9A-Z}: 同上, 控制右馬達
 * s{0-9A-Z}: 延遲多少 n*0.1 秒. 0: n=0, 9: n=9, A: n=10, Z: n=35
 * e: 停止程式

範例:

 * l0rZ 左邊停止右邊震動
 * l0r0sAlZrZsA 停止一秒. 震動一秒, 反覆循環
 * l0r0s5lZrZs5l0r0s5lZrZs5l0r0e 震動兩次後停止

## 如何製作 HOWTO

