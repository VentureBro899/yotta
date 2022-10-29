import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/auth/login',
    method: 'post',
    data
  })
}

export function getInfo(token) {
  return request({
    url: '/auth/logininfo',
    method: 'get',
    params: { token }
  })
}

export function logout(token) {
  return request({
    url: '/auth/logout',
    method: 'post',
    params: { token }
  })
}
