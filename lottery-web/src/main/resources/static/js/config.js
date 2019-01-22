var url = {
  fast_Kai: '//api.api68.com/',
  config140_2: '//1680660.com/',
  news_adurl: '//1682018.cn/view/jisusaiche/pk10kai.html',
  news_adurl_none: '//1680100.com/view/jisusaiche/pk10kai.html'
}
url.config140 = url.config140_2 + 'smallSix/'
url.SixFastUrl = url.fast_Kai + 'speedSix/'
url.photoUrl = '//images.6hch.com/'
var tools = {}
var config = { publicUrl: url.photoUrl, pageSizenum: 300, ifDebugger: false }
var oldLog = console.log
console.log = function () {
  if (config.ifDebugger) {
    oldLog.apply(console, arguments)
  } else {

  }
}
function errorFun (a) {}
function sleep (b) {
  var a = new Date()
  var c = a.getTime() + b
  while (true) {
    a = new Date()
    if (a.getTime() > c) {
      return
    }
  }
}
function headAdbgcolor () {
  var a = window.location.pathname
  if (a.indexOf('SixFast') != -1) {
    $('.bodybox .header a.fastSix').css({
      color: '#fff',
      background: '#ED2842'
    })
  } else {
    if (a.indexOf('/index') == -1) {
      $('.bodybox #HKcai a.kj').css({ color: '#fff', background: '#ED2842' })
    }
  }
}
var publictools = {}
publictools.getToken = function () {
  return window.localStorage.token
}
publictools.testWWW = function () {
  var a = window.location.href
  var b = '6hch001.com'
  if (a.indexOf(b) != -1) {
    $('.brann_img').hide()
    $('#init168').attr('href', 'https://www.1680100.com')
    $('#initwebapp').attr('href', 'https://m.6hch001.com')
    $('.news_ad').attr('href', url.news_adurl_none)
  } else {
    $('.brann_img').show()
    $('.news_ad').attr('href', url.news_adurl)
  }
}
var urlhost = window.location.host
tools.Advphoto = function () {
  $.ajax({
    type: 'get',
    url: url.config140_2 + 'advertisingPicture/getAdvertisingPicture.do',
    data: { sourceUrl: urlhost },
    dataType: 'json',
    success: function (c) {
      console.log(c)
      var d = c.result.data, b = '', e = '', a = $('.imgDiv')
      if (d == '') {
        return false
      }
      $.each(d, function (f, g) {
        if (g.position == 0) {
          b +=
            "<li><a href='" +
            g.link +
            "' target='" +
            (g.openWith == 1 ? '_blank' : '_self') +
            "'><img src='" +
            url.photoUrl +
            g.image +
            "'></a></li>"
          $('#sixadv').attr('href', g.link)
          $('#fastadv').attr('href', g.link)
        }
        if (a.length != 0) {
          if (g.position == 1) {
            e =
              "<a href='" +
              g.link +
              "' id='index_ad' target='" +
              (g.openWith == 1 ? '_blank' : '_self') +
              "'><img src='" +
              url.photoUrl +
              g.image +
              "'></a>"
          }
        }
      })
      $('.brann_img ul').html(b)
      a.html(e)
      publictools.testWWW()
    },
    error: function () {}
  })
}
function random (b, a) {
  var d = Math.random() * (a - b)
  var c = Math.round(d + b)
  c = Math.max(Math.min(c, a), b)
  return c
}
function moveBall () {
  var b = $('.canvas>div>ul>li')
  for (var d = 0; d < b.length; d++) {
    var c = random(-50, 400)
    var a = random(-50, 215)
    b[d].style.cssText = 'left:' + c + 'px;top:' + a + 'px'
  }
}
var timers1, timers2
var intervalFun = function () {
  timers1 = setInterval(function () {
    moveBall()
  }, 2000)
}
function tanwindow () {
  var a = JSON.parse(localStorage.getItem('localStorageObj'))
  if (a == null) {
    a = {}
  }
  var d = new Date()
  var b = d.getDate()
  var c = d.getHours()
  if (a.day * 1 != b * 1 && !a.dayout) {
    Adv_eject()
    a.dayout = true
    a.timeout = false
    a.day = b
    localStorage.setItem('localStorageObj', JSON.stringify(a))
  }
  if (c >= 20 && !a.timeout) {
    Adv_eject()
    a.dayout = false
    a.timeout = true
    a.day = b
    localStorage.setItem('localStorageObj', JSON.stringify(a))
  }
}
var Adv_eject = function () {
  $('#fixbox_').show()
  intervalFun()
  moveBall()
}
$(function () {
  $('#sixfastbox').load('public/sixfast_header.html', function () {
    var a = window.location.pathname
    var b = a.lastIndexOf('/')
    a = a.slice(b)
    a = a.replace('/', '').split('.')[0]
    $('.' + a).addClass('checked')
  })
  tools.Advphoto = function () {
    $.ajax({
      type: 'get',
      url: url.config140_2 + 'advertisingPicture/getAdvertisingPicture.do',
      data: { sourceUrl: urlhost },
      dataType: 'json',
      success: function (c) {
        console.log(c)
        var d = c.result.data, b = '', e = '', a = $('.imgDiv')
        if (d == '') {
          return false
        }
        $.each(d, function (f, g) {
          if (g.position == 0) {
            b +=
              "<li><a href='" +
              g.link +
              "' target='" +
              (g.openWith == 1 ? '_blank' : '_self') +
              "'><img src='" +
              url.photoUrl +
              g.image +
              "'></a></li>"
          }
          if (a.length != 0) {
            if (g.position == 1) {
              e =
                "<a href='" +
                g.link +
                "' id='index_ad' target='" +
                (g.openWith == 1 ? '_blank' : '_self') +
                "'><img src='" +
                url.photoUrl +
                g.image +
                "'></a>"
            }
          }
        })
        $('.brann_img ul').html(b)
        a.html(e)
        publictools.testWWW()
      },
      error: function () {}
    })
  }
  $('#headdivbox').load('public/head.html', function () {
    publictools.testWWW()
    tools.Advphoto()
    headAdbgcolor()
    tanwindow()
    $('#headdivbox>div').on('click', '.closespan', function () {
      $('#fixbox_').hide()
      clearInterval(timers1)
      clearInterval(timers2)
    })
    console.log('request is over!')
  })
  $('#fooderbox').load('public/fooder.html', function () {
    console.log('request is over!')
  })
})
var proto = {
  issuc: '2017019',
  Zoo: ['', '鼠', '牛', '虎', '兔', '龙', '蛇', '马', '羊', '猴', '鸡', '狗', '猪'],
  fiveLineArr: ['', '金', '木', '水', '火', '土'],
  colorArr: ['', '#F8253E', '#1FC26B', '#0093E8'],
  colorEng: ['', 'red', 'green', 'blue'],
  colorCh: ['', '红', '蓝', '绿'],
  jiaqzs: ['', '家', '野'],
  jiaqzs_text: ['', '家禽', '野兽'],
  boy_girl: ['', '男', '女'],
  top_bottom: ['', '天', '地'],
  four_season: ['', '春', '夏', '秋', '冬'],
  cqsh: ['', '琴', '棋', '书', '画']
}
$('#fooderbox,.footer').on('click', '#gotop', function () {
  $('body,html').animate({ scrollTop: 0 }, 500)
  $(this).hide()
  return false
})
$(document).scroll(function () {
  if ($(this).scrollTop() > 10) {
    $('#gotop').show()
  } else {
    $('#gotop').hide()
  }
})
$('#fooderbox,.footer').on('mousemove', '.fankuicon', function () {
  $(this).html('用户</br>反馈')
})
$('#fooderbox,.footer').on('mouseout', '.fankuicon', function () {
  $(this).html('')
})
$('#fooderbox,.footer').on('click', '.bar_remove', function () {
  $('#modal').hide()
})
$('#fooderbox,.footer').on('click', '.fankuicon', function () {
  $('#modal').show()
})
$('#fooderbox').on('click', '#btn_upload', function () {
  var a = {
    linkType: $('#fooderbox #select_op option:selected').val(),
    nickName: $('#fooderbox #first_input').val(),
    linkNumber: $('#fooderbox #fanku_text').val(),
    fedBack: $('#fooderbox #advice').val()
  }
  console.log(a)
  fankuFun(a)
})
$('.footer').on('click', '#btn_upload', function () {
  var a = {
    linkType: $('.footer #select_op option:selected').val(),
    nickName: $('.footer #first_input').val(),
    linkNumber: $('.footer #fanku_text').val(),
    fedBack: $('.footer #advice').val()
  }
  console.log(a)
  fankuFun(a)
})
function fankuFun (a) {
  $.ajax({
    type: 'get',
    url: url.config140_2 + 'fedBack/saveFedBack.do',
    async: true,
    data: a,
    dataType: 'json',
    success: function (b) {
      console.log(b)
      alert(b.result.message)
      if (b.errorCode == 0) {
        $('#modal').hide()
      }
    },
    error: function (b) {
      console.log(b)
    }
  })
}
tools.browserRedirect = function () {
  var d = navigator.userAgent.toLowerCase()
  var i = d.match(/ipad/i) == 'ipad'
  var j = d.match(/iphone os/i) == 'iphone os'
  var h = d.match(/midp/i) == 'midp'
  var e = d.match(/rv:1.2.3.4/i) == 'rv:1.2.3.4'
  var g = d.match(/ucweb/i) == 'ucweb'
  var a = d.match(/android/i) == 'android'
  if (d.indexOf('android') > 0) {
    a = true
  }
  var b = d.match(/windows ce/i) == 'windows ce'
  var k = d.match(/windows mobile/i) == 'windows mobile'
  if (i || j || h || e || g || a || b || k) {
    var f = window.location.href
    var c = '6hch001.com'
    if (f.indexOf(c) != -1) {
      window.location.href = 'http://m.6hch001.com'
    } else {
      window.location.href = 'http://m.6hch.com'
    }
  } else {
    window.location.href = 'index.html'
  }
}
