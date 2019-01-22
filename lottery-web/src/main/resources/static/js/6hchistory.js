$('.xzhm>li:not(title)').click(function () {
  var a = $(this)
    .toggleClass('active')
    .find('a')
    .attr('href')
    .replace('#zhma', '')
  $('.' + a).toggleClass('opacity')
  detailCheck()
})
// $(function () {
//   $.ajax({
//     type: 'get',
//     url: url.config140 + 'findPreDrawYearAll.do?',
//     dataType: 'json',
//     async: false,
//     success: function (c) {
//       console.log(c)
//       yearArr = c.result.data
//       var b = ''
//       for (var a = 0; a < yearArr.length; a++) {
//         b += '<li>' + yearArr[a] + '</li>'
//       }
//       $('.years_list').html(b)
//       $('.years_list>li:first-child').addClass('active')
//     }
//   })
// })
$('.xzsx>li:not(title)').click(function (b) {
  b.preventDefault()
  $(this)
    .find('a')
    .addClass('checked')
    .parent()
    .siblings()
    .find('a')
    .removeClass('checked')
  var a = $(this).find('a').attr('href').replace('#', '')
  console.log(a)
  $('.' + a).css('display', 'block').siblings('div').css('display', 'none')
})
$('.xztj>div li').click(function () {
  $(this).toggleClass('active')
  detailCheck()
})
function detailCheck () {
  $("#hmfbTable>.number_box>ul>li:contains('双')").css('color', '#F8223C')
  $("#hmfbTable>.number_box>ul>li:contains('大')").css('color', '#F8223C')
  $("#hmfbTable>.number_box>ul>li:contains('红')").css('color', '#F8223C')
  $("#hmfbTable>.number_box>ul>li:contains('蓝')").css('color', '#0093E8')
  $("#hmfbTable>.number_box>ul>li:contains('绿')").css('color', '#1FC26B')
  $("li.hisNum_detail>ul>li>span:contains('红')").css('color', '#F8223C')
  $("li.hisNum_detail>ul>li>span:contains('蓝')").css('color', '#0093E8')
  $("li.hisNum_detail>ul>li>span:contains('绿')").css('color', '#1FC26B')
  var a = $('.hisNum_detail>ul>li,.temaNum.Seven')
  var d = $('.opacity')
  var g = $('.xztj>div>ul>li.active')
  if (d.length == 0 && g.length == 0) {
    $('.hisNum_detail>ul>li,.temaNum.Seven').css('opacity', '1')
    return false
  } else {
    if (d.length == 0 && g.length != 0) {
    }
  }
  for (var f = 0; f < a.length; f++) {
    if (a[f].classList.contains('opacity') == true) {
      a[f].style.opacity = '1'
    } else {
      a[f].style.opacity = '0.1'
    }
  }
  var e = $('.xztj>div li.active').find('a').text()
  var b = $('.opacity')
  if (e.length == 0 && b.length == 0) {
    $('.hisNum_detail>ul>li,.Seven').css('opacity', '1')
  } else {
    if (e.length == 0 && b.length != 0) {
      $('.opacity').css('opacity', '1')
    }
  }
  if (b.length == 0) {
    b = $('.hisNum_detail>ul>li,.Seven')
  }
  for (var f = 0; f < b.length; f++) {
    for (var h = 0; h < e.length; h++) {
      if (b[f].getAttribute('data-text') == e[h]) {
        b[f].style.opacity = '1'
        break
      } else {
        b[f].style.opacity = '0.1'
      }
    }
  }
  var g = $('.xztj>div>ul>li.active')
}
$('.xztj>div li>input').click(function () {
  $(this).parent().siblings().removeClass('active')
})
$('.years_list').on('click', 'li', function () {
  $(this).addClass('active').siblings().removeClass('active')
  var b = $(this).text()
  $('.listhead>span').text(b + '年开奖记录')
  var a = ''
  var a = ''
  if ($('.xzsx>li .checked').length != 0) {
    a = $('.xzsx>li>.checked').parent().attr('lang')
  } else {
    a = 1
  }
  yearsFun(a, b)
})
$('.xzsx>li:not(title)').click(function () {
  var a = $(this).attr('lang')
  var b = $('.years_list>li.active').text()
  $('.xztj>div>ul .active').removeClass('active')
  yearsFun(a, b)
})
function yearsFun (a, b) {
  if (a == 1) {
    attrArr = proto.Zoo
  } else {
    if (a == 2) {
      attrArr = proto.fiveLineArr
    } else {
      if (a == 3) {
        attrArr = proto.jiaqzs
      } else {
        if (a == 4) {
          attrArr = proto.boy_girl
        } else {
          if (a == 5) {
            attrArr = proto.top_bottom
          } else {
            if (a == 6) {
              attrArr = proto.four_season
            } else {
              if (a == 7) {
                attrArr = proto.cqsh
              } else {
                if (a == 8) {
                  attrArr = proto.colorCh
                }
              }
            }
          }
        }
      }
    }
  }
  $.ajax({
    type: 'get', //post
    url: api6hc.lhc_kjjl + 'type=' + (a===''?2:a) + '&year=' + (b===''?2018:b),
    // data: { year: b, type: a },
    dataType: 'json',
    success: function (d) {
      $('.box-title').siblings().remove()
      console.log(d)
      var c = ''
      if (d.result.data == '') {
        return
      }
      $.each(d.result.data.bodyList, function (g, f) {
        var o = [], r = []
        var l = f.preDrawCode.split(',')
        for (var k = 0; k < f.color.length; k++) {
          o.push(proto.colorEng[f.color[k]])
          r.push(attrArr[f.czAndFe[k]])
        }
        if (f.seventhSingleDouble == 0) {
          f.seventhSingleDouble = '单'
        } else {
          if (f.seventhSingleDouble == 1) {
            f.seventhSingleDouble = '双'
          } else {
            f.seventhSingleDouble = '和'
          }
        }
        if (f.seventhBigSmall == 0) {
          f.seventhBigSmall = '大'
        } else {
          if (f.seventhBigSmall == 1) {
            f.seventhBigSmall = '小'
          } else {
            f.seventhBigSmall = '和'
          }
        }
        if (f.totalBigSmall == 0) {
          f.totalBigSmall = '大'
        } else {
          if (f.totalBigSmall == 1) {
            f.totalBigSmall = '小'
          } else {
            f.totalBigSmall = '和'
          }
        }
        if (f.totalSingleDouble == 0) {
          f.totalSingleDouble = '单'
        } else {
          if (f.totalSingleDouble == 1) {
            f.totalSingleDouble = '双'
          }
        }
        if ($('.aone').hasClass('active')) {
          var m = 'opacity'
        } else {
          var m = ''
        }
        if ($('.atwo').hasClass('active')) {
          var t = 'opacity'
        } else {
          var t = ''
        }
        if ($('.athree').hasClass('active')) {
          var e = 'opacity'
        } else {
          var e = ''
        }
        if ($('.afour').hasClass('active')) {
          var s = 'opacity'
        } else {
          var s = ''
        }
        if ($('.afive').hasClass('active')) {
          var q = 'opacity'
        } else {
          var q = ''
        }
        if ($('.asix').hasClass('active')) {
          var h = 'opacity'
        } else {
          var h = ''
        }
        if ($('.aseven').hasClass('active')) {
          var j = 'opacity'
        } else {
          var j = ''
        }
        // if (l[0] < 10) {
        //   l[0] = '0' + l[0]
        // }
        // if (l[1] < 10) {
        //   l[1] = '0' + l[1]
        // }
        // if (l[2] < 10) {
        //   l[2] = '0' + l[2]
        // }
        // if (l[3] < 10) {
        //   l[3] = '0' + l[3]
        // }
        // if (l[4] < 10) {
        //   l[4] = '0' + l[4]
        // }
        // if (l[5] < 10) {
        //   l[5] = '0' + l[5]
        // }
        // if (l[6] < 10) {
        //   l[6] = '0' + l[6]
        // }
        if (f.nanairo == 0) {
          f.nanairo = '红'
        } else {
          if (f.nanairo == 1) {
            f.nanairo = '绿'
          } else {
            if (f.nanairo == 2) {
              f.nanairo = '蓝'
            } else {
              if (f.nanairo == 3) {
                f.nanairo = '和局'
              }
            }
          }
        }
        if (f.seventhSingleDouble == 0) {
          f.seventhSingleDouble = '单'
        } else {
          if (f.seventhSingleDouble == 1) {
            f.seventhSingleDouble = '双'
          } else {
            if (f.seventhSingleDouble == 2) {
              f.seventhSingleDouble = '和'
            }
          }
        }
        if (f.seventhCompositeDouble == 0) {
          f.seventhCompositeDouble = '合单'
        } else {
          if (f.seventhCompositeDouble == 1) {
            f.seventhCompositeDouble = '合双'
          } else {
            if (f.seventhCompositeDouble == 2) {
              f.seventhCompositeDouble = '和'
            }
          }
        }
        if (f.seventhCompositeBig == 0) {
          f.seventhCompositeBig = '合大'
        } else {
          if (f.seventhCompositeBig == 1) {
            f.seventhCompositeBig = '合小'
          } else {
            if (f.seventhCompositeBig == 2) {
              f.seventhCompositeBig = '和'
            }
          }
        }
        if (f.seventhMantissaBig == 0) {
          f.seventhMantissaBig = '尾大'
        } else {
          if (f.seventhMantissaBig == 1) {
            f.seventhMantissaBig = '尾小'
          } else {
            if (f.seventhMantissaBig == 2) {
              f.seventhMantissaBig = '和'
            }
          }
        }
        c +=
          "<div class='hisNUm_box number_box'><ul><li class='Time_box'><span>" +
          f.preDrawDate +
          '</span><span>' +
          f.issue +
          "期</span></li><li class='hisNum_detail'><ul><li class='One " +
          m +
          "'data-text='" +
          r[0] +
          "'><span class='" +
          o[0] +
          "'>" +
          l[0] +
          '</span> <span>' +
          r[0] +
          "</span></li><li class='Two " +
          t +
          "'data-text='" +
          r[1] +
          "'><span class='" +
          o[1] +
          "'>" +
          l[1] +
          '</span> <span>' +
          r[1] +
          "</span></li><li class='Three " +
          e +
          "'data-text='" +
          r[2] +
          "'><span class='" +
          o[2] +
          "'>" +
          l[2] +
          '</span> <span>' +
          r[2] +
          "</span></li><li class='Four " +
          s +
          "'data-text='" +
          r[3] +
          "'><span class='" +
          o[3] +
          "'>" +
          l[3] +
          '</span> <span>' +
          r[3] +
          "</span></li><li class='Five " +
          q +
          "'data-text='" +
          r[4] +
          "'><span class='" +
          o[4] +
          "'>" +
          l[4] +
          '</span> <span>' +
          r[4] +
          "</span></li><li class='Six " +
          h +
          "'data-text='" +
          r[5] +
          "'><span class='" +
          o[5] +
          "'>" +
          l[5] +
          '</span> <span>' +
          r[5] +
          "</span></li></ul></li><li class='temaNum Seven " +
          j +
          "'data-text='" +
          r[6] +
          "'><span class='" +
          o[6] +
          "'>" +
          l[6] +
          '</span> <span>' +
          r[6] +
          "</span></li><li class='Daxia_dansh dansh_ '>" +
          f.sumTotal +
          "</li><li class='Daxia_dansh'>" +
          f.totalSingleDouble +
          "</li><li class='Daxia_dansh'>" +
          f.totalBigSmall +
          "</li><li class='Daxia_dansh'>" +
          f.nanairo +
          "</li><li class='Daxia_dansh'>" +
          f.seventhSingleDouble +
          "</li><li class='Daxia_dansh'>" +
          f.seventhBigSmall +
          "</li><li class='Daxia_dansh'>" +
          f.seventhCompositeDouble +
          "</li><li class='Daxia_dansh'>" +
          f.seventhCompositeBig +
          "</li><li class='Daxia_dansh'>" +
          f.seventhMantissaBig +
          '</li></ul></div>'
      })
      $('.box-title').after(c)
      detailCheck()
    }
  })
}
$(function () {
  var a = 1
  var b = $('.years_list>li.active').text()
  $('.listhead>span').text(b + '年开奖记录')
  yearsFun(a, b)
})
var zoo_Aimg, zoo_Bimg
$(function () {
  var a = setTimeout(function () {
    $('.kj').css({ color: '#fff', background: '#ED2842' })
    if ($('.kj').length != 0) {
      clearTimeout(a)
    }
  }, 100)
//   $.ajax({
//     type: 'post', //post
//     url: url.config140_2 + 'alternatePicture/findByRecently.do',
//     data: {},
//     dataType: 'json',
//     success: function (c) {
//       console.log(c)
//       var b = c.result.data
//       $.each(b, function (d, e) {
//         if (e.type == 0) {
//           zoo_Aimg = e.image
//           $('#add_ABimg img').attr('src', url.photoUrl + zoo_Aimg)
//         } else {
//           zoo_Bimg = e.image
//         }
//       })
//     },
//     error: function () {
//       console.log('ab面请求出错')
//     }
//   })
})
$('#ABimg_btn').on('click', 'button', function () {
  $(this).addClass('active').siblings().removeClass()
  if ($(this).attr('data-text') == 'A') {
    $('#add_ABimg img').attr('src', url.photoUrl + zoo_Aimg)
  } else {
    $('#add_ABimg img').attr('src', url.photoUrl + zoo_Bimg)
  }
})
