<#include "resource.ftl">
<body>
<div class="yotta-venture">
    <#include "header.ftl">
    <div class="yotta-main">
        <div class="yotta-main-left">
            <div id="yotta-carousel" class="carousel slide" data-ride="carousel" data-interval="2000">
                <!-- 圆点指示器 -->
                <ol class="carousel-indicators">
                    <#list sliders as slider>
                        <li data-target="#yotta-carousel" data-slide-to="${slider?index}" class="${(slider?index==0)?then('active','')}"></li>
                    </#list>
                </ol>

                <!-- 轮播项目 -->
                <div class="y carousel-inner">
                    <#list sliders as slider>
                    <div class="item ${(slider?index==0)?then('active','')}">
                        <a href="${slider.route}"><img alt="${slider.title}" src="${imgdomain}${slider.picture}"></a>
                        <div class="carousel-caption">
                            <h3>${slider.title}</h3>
                        </div>
                    </div>
                    </#list>
                </div>

                <!-- 项目切换按钮 -->
                <a class="left carousel-control" href="#yotta-carousel" data-slide="prev">
                    <span class="icon icon-chevron-left"></span>
                </a>
                <a class="right carousel-control" href="#yotta-carousel" data-slide="next">
                    <span class="icon icon-chevron-right"></span>
                </a>
            </div>
            <#if recommends?has_content>
                <div class="yotta-recommend">
                <div class="module-title">推荐文章</div>
                <div class="yotta-recommended-list">
                    <#list recommends as recommendArticle>
                     <a href="article/${recommendArticle.id!}">
                        <img src="${imgdomain}/${recommendArticle.coverpicture!}" class="img-responsive" alt="${recommendArticle.title!}">
                    </a>
                    </#list>
                </div>
            </div>
            </#if>
            <div class="yotta-article">
                <div class="module-title">文章列表</div>
                <div class="yotta-article-list">
                    <#list articles as article>
                    <div class="yotta-article-item animate__animated animate__fadeInDown">
                        <div class="article-img">
                            <div class="floatdate">${article.postdate}</div>
                            <img src="${imgdomain}/${article.coverpicture}" class="img-rounded" alt="圆角图片">
                        </div>
                        <div class="article-text">
                            <a href="/article/${article.id}"><h2>${article.title}</h2></a>
                            <div class="prop-list">
                                <span><i class="icon icon-beaker"></i>${article.postdate}</span>
                                <span><i class="icon icon-eye-open"></i>${article.viewsum}</span>
                                <span><i class="icon icon-thumbs-o-up"></i>${article.thumbsum}</span>
                                <span><i class="icon icon-folder-close"></i>${article.cname!}</span>
                            </div>
                            <div class="abstract">
                                ${article.description}
                            </div>
                        </div>
                    </div>
                </#list>
            </div>
        </div>
    </div>
    <#include "sider.ftl">
</div>
    <#include "footer.ftl">
</div>


</body>

</html>