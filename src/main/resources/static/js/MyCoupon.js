window.addEventListener("pageshow", (event) => {
  if (event.persisted || window.performance.getEntriesByType("navigation")[0].type === "back_forward") {
    window.location.reload();
  }
});

document.addEventListener("DOMContentLoaded", () => {
  const container = document.querySelector(".coupon-section");

  let saved = JSON.parse(localStorage.getItem("myCoupons")) || [];
  let activeCoupon = localStorage.getItem("activeCoupon");
  
  // ถ้ายังไม่มีคูปอง
  if (saved.length === 0) {
    container.innerHTML += `
      <div style="
        width: 100%;
        display: flex;
        justify-content: center;
        margin-top: 20px;
        margin-bottom: 40px;
      ">
        <p style="color:#555; font-size:16px;">ยังไม่มีคูปองที่เก็บไว้</p>
      </div>
    `;
    return;
  }


  saved.forEach(text => {
    const isActive = activeCoupon === text; // เช็คว่าคูปองนี้กำลังถูกใช้
    const html = `
      <div class="coupon-item">
        <div class="coupon-icon">
          <svg xmlns="http://www.w3.org/2000/svg" width="40" height="40" fill="currentColor" viewBox="0 0 24 24">
            <path d="M2 6v12a2 2 0 0 0 2 2h16a2 2 0 0 0 2-2v-2a2 2 0 0 1 0-4v-2a2 2 0 0 1 0-4V6a2 2 0 0 0-2-2H4a2 2 0 0 0-2 2zm6 6a2 2 0 1 1 4 0a2 2 0 0 1-4 0zm7-5h3v10h-3z"/>
          </svg>
        </div>
        <div class="coupon-text">${text}</div>
        <button class="use-btn" ${isActive ? 'disabled' : ''}>
          ${isActive ? '✓ ใช้แล้ว' : 'ใช้'}
        </button>
      </div>
    `;
    container.innerHTML += html;
  });
  
  container.querySelectorAll('.coupon-item').forEach(item => {
      const btn = item.querySelector('.use-btn');
      const text = item.querySelector('.coupon-text').textContent;
      btn.disabled = activeCoupon === text;
    });
	
  container.addEventListener("click", (e) => {
      const btn = e.target.closest(".use-btn");
      if (!btn) return;

      const couponText = btn.closest('.coupon-item').querySelector('.coupon-text').textContent;

      // ปุ่มทั้งหมดคืนค่า default ก่อน
      container.querySelectorAll('.use-btn').forEach(b => {
        b.textContent = 'ใช้';
        b.disabled = false;
      });

      // ใช้คูปองที่เลือก
      btn.textContent = '✓ ใช้แล้ว';
      btn.disabled = true;

      // เก็บคูปองที่เลือกไว้ใน localStorage
      localStorage.setItem('activeCoupon', couponText);

    });
  });
