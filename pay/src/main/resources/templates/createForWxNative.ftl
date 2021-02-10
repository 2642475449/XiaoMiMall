<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>微信支付</title>
</head>
<body>

<div id="myQrcode"></div>
<div id="orderId" hidden>${orderId}</div>
<div id="returnUrl" hidden>${returnUrl}</div>

<script src="https://cdn.bootcdn.net/ajax/libs/jquery/1.5.1/jquery.min.js"></script>
<script src="https://cdn.bootcdn.net/ajax/libs/jquery.qrcode/1.0/jquery.qrcode.min.js"></script>
<script>
    jQuery('#myQrcode').qrcode({
        text    :"${codeUrl}"
    });
    $(function () {
        setInterval(function () {
            //日志
            console.log("开始查询支付状态..")
            //通过 HTTP 请求加载远程数据
            $.ajax({
                //（默认为当前页地址）发送请求的地址
                url:'/pay/queryByOrderId',
                //要求为Object或String类型的参数，发送到服务器的数据
                data:{
                    'orderId':$('#orderId').text()
                },
                success: function (result) {
                    console.log(result)
                    if (result.platformStatus != null && result.platformStatus === 'SUCCESS') {
                        location.href= $('#returnUrl').text()
                    }
                },
                error: function (result) {
                    alert(result)
                }
            })
        },2000)
    })
</script>
</body>
</html>