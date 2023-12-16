$( document ).ready(function() {

    const listing = fetch('/.list')
        .then(res => res.json())
        .then(json => {

        })

    const createFolder = ({name}) => $(`
        <div class="tile folder">
            <i class="mdi mdi-folder"></i>
            <h3>${name}</h3>
            <p>${name}</p>
        </div>
    `)

    const createDocument = ({title}) => $(`
        <div class="tile form">
            <i class="mdi mdi-file-document"></i>
            <h3>${title}</h3>
            <p>${title}</p>
        </div>
    `)
});