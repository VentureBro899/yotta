<#include "resource.ftl">
<body>
<div class="yotta-venture">
    <#include "header.ftl">
    <div class="yotta-main">
        <div class="yotta-main-left">
            <div class="yotta-article-detail">
                <h1>${article.title}</h1>
                <div class="attrlist">
                    <span><i class="icon icon-beaker"></i>${article.postdate}</span>
                    <span><i class="icon icon-eye-open"></i>${article.viewsum}</span>
                    <span><i class="icon icon-thumbs-o-up"></i>${article.thumbsum}</span>
                    <span><i class="icon icon-folder-close"></i>${article.cname!}</span>
                    <#-- <span><i class="icon icon-comment-alt"></i>453</span> -->

                </div>
                <div class="article-body">
                    <div id="yotta-markdown">
                         <textarea id="maincontent">${article.maincontent}</textarea>
                    </div>
                    <div class="operation">
                        <div class="likethis">

                            <p class="hl-red">如果本文对你有用，就给个小爱心吧</p>


                            <label for="thumbup">
                                <input type="checkbox" id="thumbup" class="likeanimation" hidden />
                                <svg t="1639041971928" class="icon" viewBox="0 0 1024 1024" version="1.1"
                                     xmlns="http://www.w3.org/2000/svg" p-id="3128">
                                    <path
                                            d="M512 896a42.666667 42.666667 0 0 1-30.293333-12.373333l-331.52-331.946667a224.426667 224.426667 0 0 1 0-315.733333 223.573333 223.573333 0 0 1 315.733333 0L512 282.026667l46.08-46.08a223.573333 223.573333 0 0 1 315.733333 0 224.426667 224.426667 0 0 1 0 315.733333l-331.52 331.946667A42.666667 42.666667 0 0 1 512 896z"
                                            p-id="3129" id="heart"></path>
                                </svg>
                                <span></span>
                            </label>
                            <p class="text-gray" id="thumbsum">${article.thumbsum}</p>

                        </div>
                    </div>
                </div>
                <div class="article-right">
                    文章版权由博主所有，转载注明出处 https://www.996so.icu 分享技术，打击剽窃，我们一起努力
                </div>
            </div>
        </div>
        <#include "sider.ftl">
    </div>
    <#include "footer.ftl">
</div>
<script src="/js/mutual.js"></script>
<script src="/js/markdown-it.min.js"></script>
<link rel="stylesheet" href="/css/mdbeautify.min.css">
<script src="/js/highlight.min.js"></script>
<script src="/js/markdown-it-toc.min.js"></script>
 <script type="text/javascript">
    let thumbsum =  $("#thumbsum").html()

    var md = window.markdownit({
        html:true,
        breaks: true
    });
    var md = window.markdownit();
    md = md.use(window.markdownitTOC);
    var result = md.render('\n\n[TOC]\n\n' + $('#maincontent').text());
    $('#yotta-markdown').html(result)
    document.querySelectorAll('pre code').forEach(el => {
        // then highlight each
        hljs.highlightElement(el);
    });

    $(".table-of-contents:first").appendTo($(".yotta-toc"))
    $("#heart").on("click", function () {
        if (timeInterval > 0) {
            new $.zui.Messager('你的操作太频繁啦', {
                icon: null,
                time: 1000,
                close: false
            }).show();
            return
        }
        timeInterval = 5
        setTimeout(() => {
            const flag = $("#thumbup").is(':checked')
            $('#thumbup').prop('disabled', true)
            if (flag) {
                $.post("/article/thumbup/${article.id}", { "flag": true }, data => {
                }, "json")
                setTimeout(function () {
                    $("#thumbsum").html(++thumbsum)
                    new $.zui.Messager('点赞成功', {
                        icon: 'heart',
                        time: 1000,
                        close: false
                    }).show();
                }, 1200)
            } else {
                $.post("/article/thumbup/${article.id}", { "flag": false }, data => {
                    $("#thumbsum").html(--thumbsum)
                }, "json")
                new $.zui.Messager('已取消', {
                    icon: 'heart',
                    time: 1000,
                    close: false
                }).show();
            }

        }, 100)
        setTimeout(() => {
            timeInterval = 0
            $('#thumbup').prop('disabled', false)
        }, 5000)
    })
</script>
</body>

</html>