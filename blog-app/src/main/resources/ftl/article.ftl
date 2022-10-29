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
              <textarea class="maincontent">${article.maincontent}</textarea>
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
            </div>
        </div>
        <#include "sider.ftl">
    </div>
    <#include "footer.ftl">
</div>
<link rel="stylesheet" href="/plugins/editor.md/css/editormd.preview.css" />
<script src="/plugins/editor.md/js/marked.min.js"></script>
<script src="/plugins/editor.md/js/prettify.min.js"></script>

<script src="/plugins/editor.md/js/raphael.min.js"></script>
<script src="/plugins/editor.md/js/underscore.min.js"></script>
<script src="/plugins/editor.md/js/sequence-diagram.min.js"></script>
<script src="/plugins/editor.md/js/flowchart.min.js"></script>
<script src="/plugins/editor.md/js/jquery.flowchart.min.js"></script>
<script src="/plugins/editor.md/js/editormd.js"></script>
 <script type="text/javascript">
    let thumbsum =  $("#thumbsum").html()

    // 解析md
    editormd.markdownToHTML('yotta-markdown', {
        htmlDecode: 'style,script,iframe', // you can filter tags decode
        markdown: $('.maincontent').html().trim(),
        emoji: true,
        taskList: true,
        tex: true, // 默认不解析
        flowChart: true, // 默认不解析
        sequenceDiagram: true // 默认不解析
    })
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