package com.air.antispider.stream.common.bean

/**
 * 流程类：对应表itcast_process_info
 * （规则配置集合和阈值参数）
 */
case class FlowCollocation(flowId: String, flowName: String, rules: List[RuleCollocation], flowLimitScore: Double = 100,strategyCode:String)
