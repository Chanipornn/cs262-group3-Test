  const eatButton = document.getElementById('eatButton');
  const btnText = eatButton.querySelector('.btn-text');

  let orderType = localStorage.getItem("orderTypeId");

  // ถ้ายังไม่มีค่า → ตั้ง Default = ทานที่ร้าน (1)
  if (!orderType) {
    orderType = "1";
    localStorage.setItem("orderTypeId", "1");
  }

// อัปเดต UI ตามค่าที่มี
  if (orderType === "1") {
    eatButton.classList.remove("takeaway");
    btnText.textContent = "ทานที่ร้าน";
  } else {
    eatButton.classList.add("takeaway");
    btnText.textContent = "กลับบ้าน";
  }

eatButton.addEventListener('click', () => {
    const isDineInNow = !eatButton.classList.toggle('takeaway');

    if (isDineInNow) {
      btnText.textContent = 'ทานที่ร้าน';
      localStorage.setItem("orderTypeId", "1");
    } else {
      btnText.textContent = 'กลับบ้าน';
      localStorage.setItem("orderTypeId", "2");
    }
  });

  // ฟังก์ชันสำหรับปุ่ม "เก็บ" แต่ละอัน
  const claimButtons = document.querySelectorAll('.claim-btn');
  let savedCoupons = JSON.parse(localStorage.getItem("myCoupons")) || [];
  
  claimButtons.forEach(button => {
	const couponText = button.closest('.promo-item').querySelector('.promo-text').textContent;
    button.addEventListener('click', function(e) {
      if (!this.classList.contains('claimed')) {
        // เพิ่มเอฟเฟกต์กระเด้ง
        this.style.transform = 'scale(0.9)';
        setTimeout(() => {
          this.style.transform = 'scale(1)';
        }, 100);
        
        // เปลี่ยนสถานะ
        this.classList.add('claimed');
        this.innerHTML = '✓ เก็บแล้ว';
		if (savedCoupons.includes(couponText)) {
		    button.classList.add('claimed');
		    button.innerHTML = '✓ เก็บแล้ว';
		  }
		saveCouponToStorage(couponText);
        
        // สร้างอนิเมชันเช็คมาร์ค
        createCheckAnimation(this);
        
        // ตรวจสอบว่าโค้ดในหมวดนี้เก็บครบหรือยัง
        checkSectionComplete(this);
      }
    });
  });

  // ฟังก์ชันสร้างอนิเมชันเช็คมาร์ค
  function createCheckAnimation(button) {
    const card = button.closest('.promo-item');
    card.style.transition = 'all 0.3s ease';
    card.style.backgroundColor = '#e8f5e9';
    
    setTimeout(() => {
      card.style.backgroundColor = 'white';
    }, 500);
  }

  // ฟังก์ชันสำหรับปุ่ม "เก็บโค้ดทั้งหมด"
  const collectAllButtons = document.querySelectorAll('.collect-all-btn');
  
  collectAllButtons.forEach(collectBtn => {
    collectBtn.addEventListener('click', function() {
      // หา promo-list ที่อยู่ก่อนหน้าปุ่มนี้
      let currentElement = this.previousElementSibling;
      
      // วนหา promo-list ที่ใกล้ที่สุด
      while (currentElement) {
        if (currentElement.classList.contains('promo-list')) {
          break;
        }
        currentElement = currentElement.previousElementSibling;
      }
      
      if (currentElement && currentElement.classList.contains('promo-list')) {
        const buttonsInSection = currentElement.querySelectorAll('.claim-btn:not(.claimed)');
        
        // เก็บทุกโค้ดในหมวดพร้อมดีเลย์
        buttonsInSection.forEach((btn, index) => {
          setTimeout(() => {
            btn.style.transform = 'scale(0.9)';
            setTimeout(() => {
              btn.style.transform = 'scale(1)';
              btn.classList.add('claimed');
              btn.innerHTML = '✓ เก็บแล้ว';
			  const couponText = btn.closest('.promo-item').querySelector('.promo-text').textContent;
			  saveCouponToStorage(couponText);
              createCheckAnimation(btn);
            }, 100);
          }, index * 150); // ดีเลย์แต่ละอัน 150ms
        });
        
        // เปลี่ยนสีปุ่ม "เก็บโค้ดทั้งหมด" หลังจากเก็บครบ
        setTimeout(() => {
          this.classList.add('all-claimed');
          this.innerHTML = '🎉 เก็บครบแล้ว';
          this.disabled = true;
          
          // เอฟเฟกต์ฉลอง
          this.style.transform = 'scale(1.05)';
          setTimeout(() => {
            this.style.transform = 'scale(1)';
          }, 200);
        }, buttonsInSection.length * 150 + 200);
      }
    });
  });

  // ฟังก์ชันตรวจสอบว่าโค้ดในหมวดเก็บครบหรือยัง
  function checkSectionComplete(clickedButton) {
    // หาหมวดที่ปุ่มนี้อยู่
    const promoList = clickedButton.closest('.promo-list');
    const allButtons = promoList.querySelectorAll('.claim-btn');
    const claimedButtons = promoList.querySelectorAll('.claim-btn.claimed');
    
    // ถ้าเก็บครบทุกอัน
    if (allButtons.length === claimedButtons.length) {
      // หาปุ่ม "เก็บโค้ดทั้งหมด" ที่อยู่หลัง promo-list นี้
      let nextElement = promoList.nextElementSibling;
      
      while (nextElement) {
        if (nextElement.classList.contains('collect-all-btn')) {
          if (!nextElement.classList.contains('all-claimed')) {
            nextElement.classList.add('all-claimed');
            nextElement.innerHTML = '🎉 เก็บครบแล้ว';
            nextElement.disabled = true;
            
            // เอฟเฟกต์ฉลอง
            nextElement.style.transform = 'scale(1.05)';
            setTimeout(() => {
              nextElement.style.transform = 'scale(1)';
            }, 200);
          }
          break;
        }
        nextElement = nextElement.nextElementSibling;
      }
    }
  }

  // ฟังก์ชันสำหรับปุ่มย้อนกลับ
  const backButton = document.querySelector('.back-btn');
  
  if (backButton) {
    backButton.addEventListener('click', () => {
      window.history.back();
    });
  }
  
  // เก็บคูปองลง localStorage
  function saveCouponToStorage(text) {
    let saved = JSON.parse(localStorage.getItem("myCoupons")) || [];

    // ถ้าคูปองนี้ยังไม่ถูกเก็บ -> เพิ่มเข้าไป
    if (!saved.includes(text)) {
      saved.push(text);
      localStorage.setItem("myCoupons", JSON.stringify(saved));
    }
  }
  document.addEventListener("DOMContentLoaded", () => {
    let savedCoupons = JSON.parse(localStorage.getItem("myCoupons")) || [];

    const claimButtons = document.querySelectorAll('.claim-btn');
    
    claimButtons.forEach(button => {
      const couponText = button.closest('.promo-item').querySelector('.promo-text').textContent;
      // ถ้าเคยเก็บแล้ว
      if (savedCoupons.includes(couponText)) {
        button.classList.add('claimed');
        button.innerHTML = '✓ เก็บแล้ว';
      }
    });

    // เช็คว่า promo-list ไหนครบแล้ว → อัปเดตปุ่มเก็บโค้ดทั้งหมด
    const collectAllButtons = document.querySelectorAll('.collect-all-btn');
    collectAllButtons.forEach(collectBtn => {
      // หา promo-list ที่อยู่ก่อนหน้าปุ่มนี้
      let promoList = collectBtn.previousElementSibling;
      while (promoList) {
        if (promoList.classList.contains('promo-list')) break;
        promoList = promoList.previousElementSibling;
      }

      if (promoList) {
        const buttonsInSection = promoList.querySelectorAll('.claim-btn');
        const allClaimed = Array.from(buttonsInSection).every(btn => savedCoupons.includes(
          btn.closest('.promo-item').querySelector('.promo-text').textContent
        ));
        if (allClaimed) {
          collectBtn.classList.add('all-claimed');
          collectBtn.innerHTML = '🎉 เก็บครบแล้ว';
          collectBtn.disabled = true;
        }
      }
    });
  });


