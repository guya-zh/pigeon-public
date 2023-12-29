# pigeon-public
## 组件列表

- pigeon-core 基础内容
- pigeon-log-desensitization 日志脱敏（仅支持logback）

## 使用说明

- pigeon-log-desensitization
  - logback.xml使用

    ``
  <conversionRule conversionWord="msg" converterClass="cn.guyasc.pigeon.log.desensitization.DefaultDesensitizationHandler"/>
  ``
  - 新增脱敏规则
     
    参照Java SPI的开发规范，实现 cn.guyasc.pigeon.log.desensitization.rule.MatchRule 接口在resources.META-INF.services下新建 
     cn.guyasc.pigeon.log.desensitization.rule.MatchRule 文件配置自定义开发的类