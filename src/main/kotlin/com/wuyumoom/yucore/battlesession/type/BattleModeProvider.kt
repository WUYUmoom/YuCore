package com.wuyumoom.yucore.battlesession.type

import com.wuyumoom.yucore.battlesession.BattleSession

/**
 * 对战模式提供者接口
 * 第三方实现此接口以添加自定义对战模式
 */
interface BattleModeProvider {

    /**
     * 创建对战会话
     * @param config 对战配置
     * @return 创建的会话实例
     */
    fun create(): BattleSession

    /**
     * 获取模式名称（用于日志和调试）
     */
    fun getModeName(): String
}