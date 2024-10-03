let checkboxes = document.querySelectorAll('.form-check-input'); // 체크박스 선택
let quantities = document.querySelectorAll('.quantity'); // 수량 입력 필드 선택
const totalPriceDisplay = document.getElementById('totalPriceDisplay'); // 총 가격 표시 요소 선택
const discountedPriceDisplay = document.getElementById('discountedPriceDisplay'); // 할인 가격 표시 요소 선택
const itemCountDisplay = document.getElementById('itemCountDisplay'); // 아이템 수 표시 요소 선택
const discountRow = document.getElementById('discountRow'); // 할인 정보를 표시하는 행 선택
let deleteButtons = document.querySelectorAll('.delete-item'); // 삭제 버튼 선택

let previousTotal = 0; // 이전 총 가격
let couponDiscount = 0; // 쿠폰 할인율 저장 변수
let isCouponApplied = false; // 쿠폰이 적용되었는지 확인하는 변수

// 애니메이션으로 숫자 카운트하는 함수
function countAnimation(start, end, element) {
    const duration = 300; // 애니메이션 시간 (밀리초)
    const frameDuration = 1000 / 60; // 60프레임
    const totalFrames = Math.round(duration / frameDuration); // 총 프레임 수
    const increment = (end - start) / totalFrames; // 프레임당 증가량

    let current = start; // 현재 값
    let frame = 0; // 현재 프레임

    const animate = () => {
        current += increment; // 현재 값 증가
        if (frame < totalFrames) {
            element.textContent = `₩ ${Math.round(current).toLocaleString()}`; // 애니메이션 진행 중 값 업데이트
            frame++;
            requestAnimationFrame(animate); // 다음 프레임 요청
        } else {
            element.textContent = `₩ ${end.toLocaleString()}`; // 애니메이션 종료 시 최종 값 업데이트
        }
    };

    requestAnimationFrame(animate); // 애니메이션 시작
}

// 로컬 스토리지에서 장바구니 데이터를 불러와 화면에 표시하는 함수
function loadCartFromLocalStorage() {
    const cartItems = JSON.parse(localStorage.getItem('cart')) || []; // 로컬 스토리지에서 장바구니 데이터 불러오기
    const container = document.getElementById('cartItemsContainer');
    container.innerHTML = '';

    cartItems.forEach(item => {
        const cartItemHtml = `
            <div class="row mb-2 d-flex justify-content-between align-items-center cart-item">
                <div class="form-check col-md-1 col-lg-1">
                    <input class="form-check-input" type="checkbox" value="" id="flexCheckDefault"
                        data-price="${item.price}" data-item-id="${item.id}" 
                        data-name="${item.name}" data-category="${item.category}" 
                        data-image="${item.image}" >  <!-- checked 쓰면 디폴트 체크 -->
                    <label class="form-check-label" for="flexCheckDefault"></label>
                </div>
                <div class="col-md-2 col-lg-2 col-xl-1">
                    <img src="${item.image}" class="img-fluid rounded-3">
                </div>
                <div class="col-md-3 col-lg-3 col-xl-3">
                    <h6 class="text-muted">${item.category}</h6>
                    <h6 class="mb-0">${item.name}</h6>
                </div>
                <div class="col-md-2 col-lg-1 col-xl-2">
                    <input type="number" class="form-control quantity" value="${item.quantity}" min="0" data-price="${item.price}">
                </div>
                <div class="col-md-3 col-lg-2 col-xl-2 offset-lg-2">
                    <h6 class="mb-0">₩ ${parseInt(item.price).toLocaleString()}</h6>
                </div>
                <div class="col-md-1 col-lg-1">
                    <a href="#!" class="text-muted delete-item"><i class="bi bi-trash3-fill"></i></a>
                </div>
                <hr class="my-4">
            </div>
        `;
        container.insertAdjacentHTML('beforeend', cartItemHtml); // Insert new item HTML
    });

    checkboxes = document.querySelectorAll('.form-check-input');
    quantities = document.querySelectorAll('.quantity');
    deleteButtons = document.querySelectorAll('.delete-item'); // 삭제 버튼 재선택

    updateTotalPrice(); // 총 가격 업데이트
    updateItemCount(); // 선택한 아이템 수 업데이트
    addEventListeners(); // 새롭게 로드된 항목에 이벤트 리스너 추가
}

// 선택한 아이템 수를 업데이트하는 함수
function updateItemCount() {
    let count = 0; // 아이템 수 초기화

    checkboxes.forEach(checkbox => {
        if (checkbox.checked) {
            count++; // 체크된 아이템 수 증가
        }
    });

    itemCountDisplay.textContent = `${count} 개 아이템 선택`; // 아이템 갯수 업데이트
}

// 쿠폰 할인 적용 함수
function applyCouponDiscount(total) {
    if (isCouponApplied && couponDiscount > 0) {
        return total * (couponDiscount / 100); // 할인 금액 계산
    }
    return 0; // 할인 금액이 없을 경우
}

// 선택한 아이템의 숨겨진 입력을 업데이트하는 함수
function updateItemDetails() {
    const itemDetailsContainer = document.getElementById('itemDetailsContainer'); // 숨겨진 입력 필드 컨테이너 선택
    itemDetailsContainer.innerHTML = ''; // 이전 입력 초기화

    checkboxes.forEach((checkbox, index) => {
        if (checkbox.checked) {
            const category = checkbox.getAttribute('data-category');
            const itemId = checkbox.getAttribute('data-item-id'); // 아이템 ID 가져오기
            const image = checkbox.getAttribute('data-image');
            const name = checkbox.getAttribute('data-name');
            const price = checkbox.getAttribute('data-price');
            const quantity = quantities[index].value; // 수량 가져오기

            // 각 아이템에 대한 숨겨진 입력 생성
            const itemIdInput = document.createElement('input');
            itemIdInput.type = 'hidden';
            itemIdInput.name = 'itemIds';
            itemIdInput.value = itemId;
            itemDetailsContainer.appendChild(itemIdInput);

            const itemNameInput = document.createElement('input');
            itemNameInput.type = 'hidden';
            itemNameInput.name = 'names';
            itemNameInput.value = name;
            itemDetailsContainer.appendChild(itemNameInput);

            const itemPriceInput = document.createElement('input');
            itemPriceInput.type = 'hidden';
            itemPriceInput.name = 'prices';
            itemPriceInput.value = price;
            itemDetailsContainer.appendChild(itemPriceInput);

            const itemImageInput = document.createElement('input');
            itemImageInput.type = 'hidden';
            itemImageInput.name = 'images';
            itemImageInput.value = image;
            itemDetailsContainer.appendChild(itemImageInput);

            const itemCategoryInput = document.createElement('input');
            itemCategoryInput.type = 'hidden';
            itemCategoryInput.name = 'categories';
            itemCategoryInput.value = category;
            itemDetailsContainer.appendChild(itemCategoryInput);

            const itemQuantityInput = document.createElement('input');
            itemQuantityInput.type = 'hidden';
            itemQuantityInput.name = 'quantities';
            itemQuantityInput.value = quantity;
            itemDetailsContainer.appendChild(itemQuantityInput);
        }
    });
}

// 총 가격 업데이트 함수
function updateTotalPrice() {
    let total = 0; // 총 가격 초기화

    checkboxes.forEach((checkbox, index) => {
        if (checkbox.checked) {
            const itemPrice = parseInt(checkbox.getAttribute('data-price'), 10); // 아이템 가격 가져오기
            const quantity = parseInt(quantities[index].value, 10); // 수량 가져오기
            total += itemPrice * quantity; // 가격 합계 계산
        }
    });

    // 쿠폰 할인 적용
    const discount = applyCouponDiscount(total); // 쿠폰 할인 계산
    const discountedTotal = total - discount; // 할인 후 총 가격

    // 표시된 총 가격과 숨겨진 입력 필드 업데이트
    countAnimation(previousTotal, discountedTotal, totalPriceDisplay); // 총 가격 애니메이션
    countAnimation(0, discount, discountedPriceDisplay); // 할인 애니메이션
    previousTotal = discountedTotal; // 이전 총 가격 업데이트

    // 숨겨진 입력 필드에 총 가격 설정
    document.getElementById('totalPriceInput').value = discountedTotal;

    // 숨겨진 아이템 세부 정보 업데이트
    updateItemDetails();

    // 할인 금액이 있는 경우 할인 행 표시
    if (discount > 0) {
        discountRow.style.display = 'flex';
    } else {
        discountRow.style.display = 'none'; // 할인 금액이 없으면 숨김
    }
    updateItemCount(); // 아이템 수 업데이트
}

// 쿠폰 적용 버튼 클릭 이벤트
document.getElementById('applyCouponBtn').addEventListener('click', function () {
    const couponCode = document.getElementById('form3Examplea2').value; // 입력된 쿠폰 코드 가져오기

    let couponFound = false; // 쿠폰 발견 여부 변수 초기화

    // 사용 가능한 쿠폰을 반복
    for (let i = 0; i < coupons.length; i++) {
        if (coupons[i].promoCode === couponCode) { // 쿠폰 코드가 유효한지 확인
            couponDiscount = coupons[i].percent; // 쿠폰에서 할인율 설정
            isCouponApplied = true; // 쿠폰 적용 여부 변경
            couponFound = true; // 쿠폰 발견 플래그 변경
            alert(`쿠폰이 적용되었습니다: ${couponDiscount}% 할인`); // 알림 표시
            break;
        }
    }

    if (!couponFound) { // 쿠폰이 발견되지 않은 경우
        couponDiscount = 0; // 할인율 초기화
        isCouponApplied = false; // 쿠폰 적용 여부 초기화
        alert('유효하지 않은 쿠폰 코드입니다.'); // 알림 표시
    }
    updateTotalPrice(); // 총 가격 업데이트
});
// 수량 입력 필드에 대한 검증 함수
function validateQuantity(inputElement) {
    const min = 0;
    const max = 999;
    let value = parseInt(inputElement.value, 10);

    if (isNaN(value) || value < min) {
        alert("0 이상의 숫자를 입력해 주세요")
        value = 1;
    } else if (value > max) {
        alert("수량은 최대 999개 까지만 가능합니다.")
        value = 1;
    }

    inputElement.value = value; // 검증된 값을 필드에 다시 설정
}

// 항목 삭제 함수
function deleteItem(event) {
    const cartItem = event.target.closest('.cart-item');
    cartItem.remove(); // 해당 항목 제거

s
    const itemId = cartItem.querySelector('.form-check-input').getAttribute('data-item-id'); // 아이템 ID 가져오기
    // 로컬 스토리지에서 장바구니 항목 가져오기
    const cartItems = JSON.parse(localStorage.getItem('cart')) || [];
    // 해당 아이템을 제외한 새로운 배열 생성
    const updatedCartItems = cartItems.filter(item => item.id !== itemId);
    // 업데이트된 장바구니 항목을 로컬 스토리지에 저장
    localStorage.setItem('cart', JSON.stringify(updatedCartItems));

    // 삭제 후 다시 체크박스와 수량 필드를 업데이트
    checkboxes = document.querySelectorAll('.form-check-input');
    quantities = document.querySelectorAll('.quantity');

    // 총 가격 업데이트
    updateTotalPrice();
}

// 체크박스와 수량 입력 필드에 이벤트 리스너 추가하는 함수
function addEventListeners() {
    checkboxes.forEach((checkbox, index) => {
        checkbox.addEventListener('change', updateItemCount);
        checkbox.addEventListener('change', updateTotalPrice); // 체크박스 변경 시 총 가격 업데이트
    });

    quantities.forEach((quantity, index) => {
        quantity.addEventListener('input', function () {
            validateQuantity(this); // 수량 검증
            quantity.addEventListener('change', updateItemCount);
            quantity.addEventListener('change', updateTotalPrice);

            // 로컬 스토리지에서 장바구니 항목 가져오기
            const cartItems = JSON.parse(localStorage.getItem('cart')) || [];

            // 해당 체크박스의 인덱스를 찾아서 수량 업데이트
            const itemId = checkboxes[index].getAttribute('data-item-id');
            const updatedQuantity = parseInt(this.value, 10); // 업데이트된 수량

            // 장바구니 항목에서 해당 아이템을 찾아서 수량 업데이트
            const itemToUpdate = cartItems.find(item => item.id === itemId);
            if (itemToUpdate) {
                itemToUpdate.quantity = updatedQuantity; // 수량 업데이트
            }

            // 업데이트된 장바구니 항목을 로컬 스토리지에 저장
            localStorage.setItem('cart', JSON.stringify(cartItems));
        });
    });


    deleteButtons.forEach((button) => {
        button.addEventListener('click', deleteItem); // 삭제 버튼 클릭 시 항목 삭제
        button.addEventListener('change', updateItemCount);
        button.addEventListener('change', updateTotalPrice);
    });
}

// 페이지가 로드될 때 실행되는 함수
window.onload = function () {
    loadCartFromLocalStorage(); // 로컬 스토리지에서 장바구니 데이터 로드
    addEventListeners(); // 이벤트 리스너 추가
};
