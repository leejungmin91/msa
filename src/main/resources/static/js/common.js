function formDataToJson(form) {
    const formArray = form.serializeArray();
    const data = {};

    formArray.forEach(({name, value}) => {
        data[name] = value;
    });

    return data;
}
