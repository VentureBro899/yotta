<#include "resource.ftl">
<body>
<div class="yotta-venture">
    <#include "header.ftl">
	<div class="yotta-main">
		<div class="yotta-main-left" style="background: none">
			<div class="cate-article-list">
				<#list pageInfo.list as article>
				<div class="cate-article-item">
					<div class="article-img">
						<img src="${imgdomain}/${article.coverpicture}" />
					</div>
					<div class="article-text">
						<a href="/article/${article.id}">
							<h2>${article.title}</h2>
						</a>
						<div class="prop-list">
							<span><i class="icon icon-beaker"></i>${article.postdate}</span>
							<span><i class="icon icon-eye-open"></i>${article.viewsum}</span>
							<span><i class="icon icon-thumbs-o-up"></i>${article.thumbsum}</span>
							<#-- <span><i class="icon icon-comment-alt"></i>453</span> -->
						</div>
						<div class="abstract">${article.description}</div>
					</div>
				</div>
                </#list>
			</div>
			<ul class="pager pager-pills pager-loose">
				<#if pageInfo.hasPreviousPage>
					<li class="previous"><a href="/list/${category.id}/10/${pageInfo.pageNum - 1}">«</a></li>
				</#if>

				<#list pageSum as item>
					<#if item=pageInfo.pageNum>
						<li class="active"><a href="/list/${category.id}/10/${item}">${item}</a></li>
					<#else>
						<li><a href="/list/${category.id}/10/${item}">${item}</a></li>
					</#if>
				</#list>
				<#if pageInfo.hasNextPage>
					<li class="next"><a href="/list/${category.id}/10/${pageInfo.pageNum + 1}">»</a></li>
				</#if>
			</ul>
		</div>
        <#include "sider.ftl">
	</div>
    <#include "footer.ftl">
</div>

</body>

</html>