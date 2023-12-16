$(document).ready(function () {

    fetch('/.list')
        .then(res => res.json())
        .then(json => {
            const listing = $(createTile(json, 0))
            listing.find("a").click(function (e) {
                $(this).siblings("ul").slideToggle("slow");
                e.preventDefault();
            });
            attachHandlers(listing)
            $('.box').replaceWith(listing)
        })

    function attachHandlers(root){
        const defaultTrigger = 'click'
        const defaultMethod = 'get'
        const defaultReplace = 'inner'

        $(root).find("[data-md-get],[data-md-post],[data-md-put],[data-md-delete],[data-md-trigger],[data-md-replace]")
            .each((i, e) => {
                const el = $(e)
                const method = el.data('md-get')? "get" :
                    el.data('md-post')? "post" :
                        el.data('md-put')? "put" :
                            el.data('md-delete')? "delete" : defaultMethod
                const url = el.data(`md-${method}`).replaceAll("\+", "/")
                const trigger = el.data('md-trigger') || defaultTrigger
                const target = el.data('md-target')
                const replace = el.data('md-replace') || defaultReplace

                el.on(trigger, function(){
                    $.ajax({
                        method,
                        url,
                        success: function(txt){
                            const component = document.createTextNode(txt)
                            const parent = $(target);
                            switch(replace){
                                case 'inner':
                                    parent.html(component)
                                    break
                                default:
                                    parent.replaceWith(component)
                                    break
                            }
                        }
                    })
                })
            })
    }

    function createTile(ctx, idx) {
        return `
        <ul class=${idx === 0 ? "directory-list" : ""}>
            ${ctx.constructor.name === 'String'
            ?
            `<li class="doc" data-md-get="/${ctx}" data-md-trigger="click" data-md-target="#content" data-md-replace="inner">${ctx}</li>`
            :
            ctx.constructor.name === 'Array'
                ?
                ctx.map(item => createTile(item, idx + 1)).join("")
                :
                Object.keys(ctx).map(key => (
                    `<li class="folder"><a href="#">${key}</a>
                        ${createTile(ctx[key], idx + 1)}
                    </li>`)
                ).join("")
        }
        </ul>`.trim().replace(/\n\s+/g, "")
    }
});

