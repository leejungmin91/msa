function renderPagination(pageData, func) {
    let paginationHtml = '<ul class="pagination justify-content-center">';

    for (let i = 0; i < pageData.totalPages; i++) {
        paginationHtml += `
            <li class="page-item ${i === pageData.currentPage ? 'active' : ''}">
                <a class="page-link" href="#" data-page="${i}">${i + 1}</a>
            </li>`;
    }

    paginationHtml += '</ul>';
    $('#pagination').html(paginationHtml);

    // 이벤트 바인딩
    $('#pagination .page-link').on('click', function (e) {
        e.preventDefault();
        const page = $(this).data('page');
        func(page); // 전달받은 함수 실행
    });
}