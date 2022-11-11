<div class="yotta-main-right">
	<div class="yotta-own-cart">
		<div class="topbg"></div>
		<div class="avatar"><img src="${imgdomain}${site.site_owner_avatar}"></div>
		<div class="motto">
			<span>${site.site_owner}</span>
			<em>${site.site_owner_motto}</em>
		</div>
		<div class="info">
			<div class="statistic"><span class="sum">${statistic.articleCount}</span><span class="prop">文章数</span></div>
			<div class="statistic"><span class="sum">${statistic.thumbCount}</span><span class="prop">点赞数</span></div>
			<div class="statistic"><span class="sum">${statistic.viewCount}</span><span class="prop">浏览量</span></div>
		</div>
	</div>
	<div class="yotta-hot">
		<div class="title"><i class="icon icon-star"></i>最热文章</div>
		<div class="hot-list">
			<#list hotArticles as hotArticle>
				<a href="/article/${hotArticle.id}"><div class="hot-item">${hotArticle.title}</div></a>
            </#list>
		</div>
	</div>
	<div class="yotta-toc"></div>
</div>