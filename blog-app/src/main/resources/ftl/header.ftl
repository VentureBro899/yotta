<div class="yotta-header">
    <div class="yotta-container animate__animated ">
        <div class="yotta-logo"></div>
        <div class="yotta-mobile-maincontain">
            <div class="topbg"></div>
            <div class="ownerinfo">
                <img src="${imgdomain}${site.site_owner_avatar}">
                <div class="myinfo">
                    <h3>${site.site_owner}</h3>
                    <small>${site.site_owner_motto}</small>
                </div>
            </div>
        </div>
        <#macro navtree navigators>
            <#if navigators?has_content>
                <ul class="dropdown-menu">
                    <#list navigators as navigator>
                        <li class="dropdown-submenu">
                            <a href="${navigator.children?has_content?
                            then('javascript:void(0)',navigator.route!'/list/' + navigator.cid +'/10/1')}">
                                <i class="icon ${(navigator.icon)!}"></i>${(navigator.title)!}
                            </a>
                            <@navtree navigators=navigator.children></@navtree>
                        </li>
                    </#list>
                </ul>
            </#if>
        </#macro>
        <div class="yotta-nav-list">
            <#list navigators as navigator>
                <div class="yotta-nav-item dropdown dropdown-hover">
                    <#if navigator.children?has_content>
                        <div><i class="icon icon-file-archive"></i>${navigator.title}</div>
                    <#else>
                        <a href="${navigator.route!'/list/' + navigator.cid +'/10/1'}">
                            <div><i class="icon ${(navigator.icon)!}"></i>${navigator.title}</div>
                        </a>
                    </#if>
                    <@navtree navigators=navigator.children></@navtree>
                </div>
            </#list>
        </div>
        <div class="yotta-search">
            <div class="input-group">
                <div class="input-control search-box search-box-circle has-icon-left has-icon-right search-example"
                     id="searchboxExample">
                    <input id="searchForDesktop" type="search" class="form-control search-input" placeholder="全站搜索" />
                    <label for="searchForDesktop" class="input-control-icon-left search-icon"><i
                                class="icon icon-search"></i></label>
                </div>
                <span class="input-group-btn">
              <button class="btn btn-primary" type="button" onclick="dsearch()">搜索</button>
            </span>
            </div>
        </div>
    </div>
    <div class="mobile-nav">
        <div class="sidermasker animate__animated"></div>
        <i class="icon icon-server showSider"></i>
        <i class="icon icon-search showSearch"></i>
        <div class="searchframe animate__animated">
            <input id="searchForMobile" type="search" class="form-control search-input" placeholder="全站搜索" />
            <div class="onsearch" onclick="msearch()"><i class="icon icon-search"></i></div>
        </div>
    </div>
</div>