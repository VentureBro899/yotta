import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },

  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },

  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [{
      path: 'dashboard',
      name: 'Dashboard',
      component: () => import('@/views/dashboard/index'),
      meta: { title: '仪表盘', icon: 'dashboard' }
    }]
  },

  {
    path: '/article',
    component: Layout,
    redirect: '/article/list',
    name: 'Article',
    meta: { title: '文章管理', icon: 'form' },
    children: [
      {
        path: 'add',
        name: 'articleadd',
        component: () => import('@/views/article/add'),
        meta: { title: '发布文章', icon: 'el-icon-upload', keepAlive: true }
      },
      {
        path: 'list',
        name: 'articlelist',
        component: () => import('@/views/article/list'),
        meta: { title: '文章列表', icon: 'el-icon-s-operation' }
      },
      {
        path: 'update/:id',
        name: 'articleupdate',
        component: () => import('@/views/article/update'),
        hidden: true,
        meta: { title: '更新文章', icon: 'tree' }
      }
    ]
  },

  {
    path: '/slider',
    component: Layout,
    redirect: '/slider/list',
    name: 'Slider',
    meta: { title: '轮播管理', icon: 'el-icon-s-management' },
    children: [
      {
        path: 'list',
        name: 'sliderList',
        component: () => import('@/views/slider/list'),
        meta: { title: '轮播列表', icon: 'el-icon-s-unfold' }
      },
      {
        path: 'add',
        name: 'slideradd',
        component: () => import('@/views/slider/add'),
        meta: { title: '添加轮播', icon: 'el-icon-circle-plus' }
      },
      {
        path: 'update/:id',
        name: 'sliderUpdate',
        component: () => import('@/views/slider/update'),
        hidden: true,
        meta: { title: '更新轮播', icon: 'form' }
      }
    ]
  },

  {
    path: '/category',
    component: Layout,
    redirect: '/category/list',
    name: 'Category',
    meta: { title: '分类管理', icon: 'el-icon-menu' },
    children: [
      {
        path: 'list',
        name: 'catelist',
        component: () => import('@/views/cate/list'),
        meta: { title: '分类管理', icon: 'el-icon-menu' }
      }
    ]
  },
  {
    path: '/navigator',
    component: Layout,
    name: 'Navigator',
    redirect: '/navigator/list',
    meta: { title: '导航管理', icon: 'el-icon-location-information' },
    children: [
      {
        path: 'list',
        name: 'navigatorList',
        component: () => import('@/views/navigator/list'),
        meta: { title: '导航树', icon: 'el-icon-location-information' }
      }
    ]
  },
  {
    path: '/site/general',
    component: Layout,
    redirect: '/site/general',
    name: 'Site',
    children: [
      {
        path: 'general',
        name: 'general',
        component: () => import('@/views/site/general'),
        meta: { title: '网站配置', icon: 'el-icon-s-tools' }
      }
    ]
  },

  {
    path: 'external-link',
    component: Layout,
    children: [
      {
        path: 'https://996so.icu',
        meta: { title: 'venture blog', icon: 'link' }
      }
    ]
  },

  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
