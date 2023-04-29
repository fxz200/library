package com.example.myapplication

enum class AnchorStatus(i: Int) {

    EMPTY(0),   //当前没有锚点
    HOSTING(1), //锚点正在同步
    HOSTED(2),   //锚点同步成功
    HOST_FAILED(3)   //锚点同步失败

}