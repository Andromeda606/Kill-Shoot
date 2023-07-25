<p align="center" style="font-size: larger;">
  <img src="app/src/main/res/mipmap-xxhdpi/ic_launcher.png" alt="Kill-Shoot" />
</p>

<p align="center" style="font-size: xx-large">Kill-Shoot</p>

<p align="center" style="font-size: larger; display: flex;flex-wrap: wrap;width: 100%;gap: 3em">
  <img src="github/screenshot1.jpg"/>
  <img src="github/screenshot2.jpg"/>
  <img src="github/screenshot3.jpg"/>
  <img src="github/screenshot4.jpg"/>
</p>

Kill-Shoot tamamen android java ile yazılmış bir 2D savaş oyunudur.
## Oyunun amacı
Amacınız karşıdaki kişiyi kurşunlar ile öldürmektir.
## Nasıl çalışıyor?
ImageView kullanarak kullanıcının attığı kurşun diğer tarafa animasyon kullanılarak gönderilir. Bu esnada x y konumunu 20 ms de bir kontrol eden bir collision dedektörü var. Dedektörler otomatik olarak Rect kullanarak kullanıcının ve kurşunun konumuna göre detect eder. Eğer yakalanırsa canı düşürür ve resmi havuzdan siler.
## Bilgi
Bu program "Nesne Yönelimli Programlar" dersinin proje ödevi için yapılmıştır.