if ($("#openType").val() == "alarm") {
    $("#btnAlarm").trigger("click");
}


//创建一张图片
function createImage(data) {
    var imageGroup = d3.select("svg")
        .append("g")
        .classed("ele", true)
        .data([data]);

    imageGroup.append("image")
        .classed("shape-image", true)
        .attr("x", function (d) {
            return d.x
        })
        .attr("y", function (d) {
            return d.y
        })
        .attr("width", function (d) {
            return d.width
        })
        .attr("height", function (d) {
            return d.length
        })
        .attr("xlink:href", function (d) {
            if (d.status)
                return d.openUrl;
            return d.closeUrl;
        });


    // imageGroup.attr("transform", function (d) {
    //     return "rotate(" + d.radius + "," + d.x2 + "," + d.y2 + ")";
    // });

    imageGroup.on("click", openConfirmDialog);

}



//创建元素
function createElement(ele) {
    // if (ele.type == "image") {
        createImage(ele);
    // }
}

function openConfirmDialog(d) {
//    console.log(d.href);
    sendCommand(d)

    // var htmlStr = '';
    // htmlStr += '<input type="hidden" id="channel_id" value="0">';
    // htmlStr += '<div class="weui-mask"></div>';
    // htmlStr += '<div class="weui-dialog__hd"><strong class="weui-dialog__title">弹窗标题</strong></div>';
    // htmlStr += '<div class="weui-dialog__bd">';
    // htmlStr += d.title + '</div>';
    // htmlStr += '</div>';
    // htmlStr += '<div class="weui-dialog__ft">';
    // htmlStr += '<a id="qvxiao" href="javascript:void (0);" class="weui-dialog__btn weui-dialog__btn_default">取消</a>';
    // htmlStr += '<a id="queding" href="javascript:void (0);" class="weui-dialog__btn weui-dialog__btn_primary">确定</a>';
    // htmlStr += '</div></div>';

    // var tips = d.status ? "是否关闭" : "是否打开";
    //
    // $("#title")
    //     .text('关闭' + d.title);
    // $("#alertDialog")
    //     .show()
    //     .data("data", d)
    // $("#queding")
    //     .click(function () {
    //         sendCommand(d)
    //         $("#alertDialog").hide();
    //
    //     });
    // $("#qvxiao")
    //     .click(function () {
    //         $("#alertDialog").hide();
    //     });
}

function sendCommand(d) {
//    console.log('channel_id为：' + d.channel_id);
    control.switchChannel(d.channel_id+","+d.title+","+d.status+","+d.datapoint+","+d.din);
}

//加载容器属性和元件
// initContainer();
// setInterval("refreshContainer()", 2000);


var tokens;
var house_ids;

function init(token, house_id) {
    tokens = token;
    house_ids = house_id;
    //加载容器属性和元件
    initContainerByToken(tokens,house_ids);
    setInterval("refreshContainerByToken(tokens,house_ids)", 8000);
}


function refreshContainerByToken(token, house_id) {

//    console.log("开始请求")

    $.ajax({
        url: 'http://qqtitapp.suntrans-cloud.com/api/v1/floor_plan_web',
        // url: 'http://192.168.2.234:8080/JsonServlet',
        // data: {'ruleId':$("#ruleId").val(),'dtuSn':$("#dtuSn").val()},
        method: 'POST',
        dataType: "json",
        // headers: {
        //     'Authorization': token
        // },

//        beforeSend: function (xhr) {
//            // token = window.localStorage.getItem('token');
//            xhr.setRequestHeader("Authorization", token);
//        },

        data: {'house_id': house_id},
        success: function (json) {

            // if (json.code == 200) {
                var con = json.container;
                if (con) {
                   var width = con.width;
                  var  height = con.height;
					scale = $("body").width()/width;
                     $("div.full-wrapper").css("height",height*scale);
                   $("svg.designer").css("transform","scale("+scale+")");
                    $("svg.designer").css("width", con.width);
                    $("svg.designer").css("height", con.height);
                    $("svg.designer").css("background-color", con.bgColor);
                    $("svg.designer").css("background-image", "url(" + con.bgImage + ")");
                    $("svg.designer").empty();
                    $("body").css("background-color", con.bgColor);
                }
                // json.signals.map(function(signal){
                //  if(signal){
                // 	 signalMap[''+signal.id]=signal;
                //  }
                // });
                json.elements.map(createElement);
            // }
        },

    });
    // refreshAlarm();
}

//初始化容器
function initContainerByToken(token, house_id) {
    $.ajax({
        // url: 'http://192.168.2.234:8080/JsonServlet',
        url: 'http://qqtitapp.suntrans-cloud.com/api/v1/floor_plan_web',
        // data: {'ruleId': $("#ruleId").val(), 'dtuSn': $("#dtuSn").val()},
        method: 'POST',
        dataType: "json",
//        headers: {
//            'Authorization': token
//        },
        // beforeSend: function (xhr) {
        //     // token = window.localStorage.getItem('token');
        //     xhr.setRequestHeader("Authorization", token);
        // },

        data: {'house_id': house_id},
        success: function (json) {
            //初始化容器样式
            // if (json.result) {
                var con = json.container;
                if (con) {
                  var   width = con.width;
                                    var  height = con.height;
                    					scale = $("body").width()/width;
                                         $("div.full-wrapper").css("height",height*scale);
                                       $("svg.designer").css("transform","scale("+scale+")");
                                        $("svg.designer").css("width", con.width);
                                        $("svg.designer").css("height", con.height);
                                        $("svg.designer").css("background-color", con.bgColor);
                                        $("svg.designer").css("background-image", "url(" + con.bgImage + ")");
                                        $("svg.designer").empty();
                                        $("body").css("background-color", con.bgColor);
                }
                // json.signals.map(function(signal){
                //  if(signal){
                // 	 signalMap[''+signal.id]=signal;
                //  }
                // });
                json.elements.map(createElement);
            }
        // }
    });
    console.log("初始化请求")
}