/**
 *  배송지 리스트 모달 JS
 */
$(document).on('click', '#openModalBtn', function () {
    loadShippingList();
    $('#shippingModalOverlay').fadeIn();
    $('#shippingModal').fadeIn();
    $('#shippingAddModal').hide();
    $('body').addClass('modal-open');
});

// 모달 닫기
$(document).on('click', '.modal-close', function () {
    $('#shippingModalOverlay').fadeOut();
    $('body').removeClass('modal-open');
});

$(document).on('click', '#openAddShippingBtn',function () {
    $('#shippingModal').hide();
    $('#shippingAddModal').show();
});

// ← 뒤로가기
$(document).on('click', '#backToListBtn',function () {
    $('#shippingList')
        .hide() // 이전 리스트 잠깐이라도 안 보이게
        .html('<div class="loading">불러오는 중...</div>');

    $('#shippingAddModal').hide();
    $('#shippingModal').show();

    loadShippingList();
});

// 선택 버튼 클릭 시
$(document).on('click', '#shippingSelectBtn',function () {
    const itemStr = $(this).data('item');
    const item = JSON.parse(decodeURIComponent(itemStr));

    alert(`${item.subject} 배송지가 선택되었습니다.`);
    selectShippingData(item);

    $('#shippingModal').fadeOut();
    $('#shippingModalOverlay').fadeOut();
    $('body').removeClass('modal-open');
});

// 배송지 삭제 버튼 클릭 시
$(document).on('click', '#shippingDeleteBtn',function () {
    if (confirm('배송지를 삭제하시겠습니까?')) {
        const id = $(this).data('id');

        deleteShipping(id);
    }
});

// 수정 버튼 클릭 시
$(document).on('click', '#shippingUpdateBtn',function () {

    const id = $(this).data('id');

    getShipping(id);
});

// 배송지 리스트 불러오기
function loadShippingList() {
    GetAjax(`/shipping/@me`, null, true).then((shipping) => {
        const count = shipping.length;
        const html = shipping.map(shippingTemplate).join('');
        if(count > 0) {
            $("#shippingList").html(html).fadeIn(); // ✅ 완료 후에 보여주기
        } else {
            $('#shippingList').html('<div class="empty">배송지가 없습니다.</div>').fadeIn();
        }
    });
}

// 배송지 리스트 템플릿
function shippingTemplate(item) {
    const itemData = encodeURIComponent(JSON.stringify(item));
    const pathname = location.pathname;
    return `
                <div class="shipping-card">
                    <div class="shipping-info">
                        <div class="name-row">
                            <strong>${item.subject}</strong>
                            <span class="${item.mainYn === 'Y' ? 'label-main' : 'label-basic'}">${item.mainYn === 'Y' ? '기본 배송지' : '배송지'}</span>
                        </div>
                        <div class="address">${item.address} ${item.addressDetail}</div>
                        <div class="phone">${item.name} ${item.phone}</div>
                    </div>
                    <div class="shipping-actions">
                        <button class="btn btn-light" id="shippingDeleteBtn" style="border-radius: 8px;" data-id="${item.id}">삭제</button>
                        <button class="btn btn-light" id="shippingUpdateBtn" style="border-radius: 8px;" data-id="${item.id}">수정</button>
                        ${pathname !== '/member' ? '<button class="btn btn-primary" id="shippingSelectBtn" style="border-radius: 8px;" data-item="'+itemData+'">선택</button>' : ''}                        
                    </div>
                </div>
    `;
}

/**
 * 배송지 추가 모달 JS
 */

// 배송지 기본배송지 체크박스 동작
$(document).on('change', '#mainYn', function () {
    if ($(this).is(':checked')) {
        $('#mainYnValue').val('Y');
    } else {
        $('#mainYnValue').val('N');
    }
});

// 배송지 저장 버튼
$(document).on('click', '#saveShipping',function () {
    const data = formDataToJson($("#addShippingForm"));

    console.log(data);

    const id = data.id ?? null;

    if(id) {
        updateShipping(id, data);
    } else {
        saveShipping(data);
    }
});

function getShipping(id) {
    GetAjax(`/shipping/@me/`+id, null, true).then((data) => {
        const form = $("#addShippingForm");
        form.find('[name="id"]').val(data.id);
        form.find('[name="subject"]').val(data.subject);
        form.find('[name="name"]').val(data.name);
        form.find('[name="phone"]').val(data.phone);
        form.find('[name="zipcode"]').val(data.zipcode);
        form.find('[name="address"]').val(data.address);
        form.find('[name="addressDetail"]').val(data.addressDetail);
        form.find('[name="mainYn"]').prop('checked',data.mainYn === 'Y');

        $('#shippingModal').hide();
        $('#shippingAddModal').show();
    });
}

// 배송지 삭제
function deleteShipping(id) {
    DeleteAjax(`/shipping/@me/`+id, null, true).then(() => {
        alert('삭제되었습니다.');
        loadShippingList();
    });
}

// 배송지 수정
function updateShipping(id, data) {
    PutAjax(`/shipping/@me/`+id, data, true).then(() => {
        $("#addShippingForm")[0].reset();
        alert('수정되었습니다.');
        $('#backToListBtn').trigger('click');
    });
}

function saveShipping(data) {
    PostAjax(`/shipping/@me`, data, true).then(() => {
        $("#addShippingForm")[0].reset();
        alert('저장되었습니다.');
        $('#backToListBtn').trigger('click');
    });
}

$(document).on("click", "#searchAddress", function() {
    // 주소검색 추가
    new daum.Postcode({
        oncomplete: function(data) {
            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분입니다.
            // 예제를 참고하여 다양한 활용법을 확인해 보세요.
            const form = $("#addShippingForm");
            form.find('[name="zipcode"]').val(data.zonecode);
            form.find('[name="address"]').val(data.address);
            //$("#zipcode").val(data.zonecode);
        }
    }).open();
});
