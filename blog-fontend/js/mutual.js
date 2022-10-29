let timeInterval = 0

$(function () {
  /* const nav = document.querySelectorAll(".yotta-nav-item")
  nav.forEach((item, index) => {
    item.addEventListener("mouseover", () => {
      item.classList.add('active');
      item.firstChild.classList.add('animate__animated', 'animate__heartBeat');
      item.style.color = '#0092ff'
    })
    item.addEventListener("mouseout", () => {
      item.classList.remove('active');
      item.firstChild.classList.remove('animate__animated', 'animate__heartBeat');
      item.style.color = 'black'
    })
    
  }) */
  $(".yotta-nav-item").on("mouseover", function () {
    $(this).addClass('active')
    $(".yotta-nav-item div i").addClass('animate__animated animate__heartBeat')
    $(this).css("color", '#0092ff')
  })
  $(".yotta-nav-item").on("mouseout", function () {
    $(this).removeClass('active')
    $(".yotta-nav-item div i").removeClass('animate__animated animate__heartBeat')
    $(this).css("color", 'black')
  })

  // 下拉导航动画
  $(".yotta-nav-item .dropdown-menu").addClass("animate__flipInX animate__animated")

  // 推荐的图片动画
  $(".yotta-recommended-list a img").on("mouseover", function () {
    $(this).addClass('animate__animated animate__pulse animate__repeat-3')
  })
  $(".yotta-recommended-list a img").on("mouseout", function () {
    $(this).removeClass('animate__animated animate__pulse')
  })
  $(".mobile-nav .showSider").on("click", function () {
    //遮罩层
    $(".mobile-nav .sidermasker").css("display", "block")
    $(".mobile-nav .sidermasker").removeClass("animate__fadeOut")
    $(".mobile-nav .sidermasker").addClass("animate__fadeInRight")

    //侧边栏
    $(".yotta-header .yotta-container").css("display", "block")
    $(".yotta-header .yotta-container").removeClass('animate__fadeOutLeft')
    $(".yotta-header .yotta-container").addClass('animate__fadeInLeft')
  })
  //点击遮罩层就收起侧边栏
  $(".mobile-nav .sidermasker").on("click", function () {
    //遮罩层
    $(".mobile-nav .sidermasker").css("display", "none")
    $(".mobile-nav .sidermasker").removeClass("animate__fadeInRight")
    $(".mobile-nav .sidermasker").addClass("animate__fadeOut")
    //侧边栏
    $(".yotta-header .yotta-container").removeClass('animate__fadeInLeft')
    $(".yotta-header .yotta-container").addClass('animate__fadeOutLeft')
    //优化 防止拉大屏幕导航错位
    setTimeout(function () {
      $(".yotta-header .yotta-container").removeClass('animate__fadeOutLeft')
      $(".yotta-header .yotta-container").css("display", "none")
    }, 500)
  })

  $(".mobile-nav .showSearch").on("click", function () {
    if ($(".mobile-nav .searchframe").css("visibility") == "hidden") {
      $(".mobile-nav .searchframe").css("visibility", "visible")
      $(".mobile-nav .showSearch").removeClass("icon-search")
      $(".mobile-nav .showSearch").addClass("icon-remove-circle")
      $(".mobile-nav .searchframe").removeClass("animate__slideOutUp")
      $(".mobile-nav .searchframe").addClass("animate__slideInDown")
    } else {
      // 图标延迟变化
      setTimeout(function () {
        $(".mobile-nav .showSearch").removeClass("icon-remove-circle")
        $(".mobile-nav .showSearch").addClass("icon-search")
      }, 250)
      $(".mobile-nav .searchframe").removeClass("animate__slideInDow")
      $(".mobile-nav .searchframe").addClass("animate__slideOutUp")
    }
  })
})
//搜索
function dsearch() {
  const key = $("#searchForDesktop").val()
  if (key.length > 0)
    location.href = "/search/10/1/" + key
  else
    new $.zui.Messager('关键字不能为空', {
      type: 'warning' // 定义颜色主题
    }).show();
}
function msearch() {
  const key = $("#searchForMobile").val()
  if (key.length > 0)
    location.href = "/search/10/1/" + key
  else
    new $.zui.Messager('关键字不能为空', {
      type: 'warning' // 定义颜色主题
    }).show();
}