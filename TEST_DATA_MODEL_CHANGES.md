# 数据模型类测试变更说明

> 测试类型: 数据模型类 (DO/DTO/VO/PO)  
> 测试目的: 验证分析程序能否正确识别数据模型类的各种变更

## 📋 变更文件清单

| 序号 | 文件路径 | 类型 | 变更类型 | 参考 Task #110 |
|------|---------|------|----------|---------------|
| 1 | `model/OrderDO.java` | DO | 字段新增(6个) | OfTransactionDO.java |
| 2 | `dto/OrderDTO.java` | DTO | 字段新增(6个) | OfTransactionDTO.java |
| 3 | `dto/OrderExportDTO.java` | DTO | 注解修改 | CommonCeBalanceExportDTO.java |
| 4 | `vo/OrderDetailVO.java` | VO | 新增类 | OfBalanceOccupyDetailVO.java |
| 5 | `pojo/OrderQueryPO.java` | PO | 新增类 | IcbcQueryDetailPO.java |

---

## 1️⃣ OrderDO.java - 实体类字段新增

### 变更类型
- **DO (实体类)** - 字段新增

### 变更内容
新增 6 个字段，用于支持双确真业务场景：

```java
// 新增字段
private Long ceGroupCompanyId;        // 核企集团公司ID
private String ceGroupCompanyName;    // 核企集团公司名称
private String ceConfirmMode;         // 核企确真方式
private String ceConfirmLevel;        // 核企确真层级
private String agwCheckNode;          // 网关流程审核节点
private String agwHandler;            // 网关经办人
```

### 测试验证点
- [ ] 能否识别 DO 类的字段新增
- [ ] 能否正确统计新增字段数量 (6个)
- [ ] 能否识别字段类型 (Long, String)
- [ ] 能否识别字段注释

---

## 2️⃣ OrderDTO.java - 数据传输对象字段新增

### 变更类型
- **DTO (数据传输对象)** - 字段新增

### 变更内容
新增 6 个字段，包括业务字段和查询条件字段：

```java
// 业务字段
private Long ceGroupCompanyId;
private String ceGroupCompanyName;
private String agwCheckNode;
private String agwHandler;

// 查询条件字段
private LocalDateTime createTimeStart;
private LocalDateTime createTimeEnd;
```

### 测试验证点
- [ ] 能否识别 DTO 类的字段新增
- [ ] 能否区分业务字段和查询条件字段
- [ ] 能否识别 LocalDateTime 类型
- [ ] 能否识别字段用途（通过注释）

---

## 3️⃣ OrderExportDTO.java - 导出类注解修改

### 变更类型
- **DTO (导出类)** - 注解修改

### 变更内容
修改 `@ExcelProperty` 注解值，将单位从"万元"改为"元"：

```java
// 变更前: @ExcelProperty("订单金额（万元）")
// 变更后: @ExcelProperty("订单金额（元）")

@ExcelProperty("订单金额（元）")
private BigDecimal amount;

@ExcelProperty("已支付金额（元）")
private BigDecimal paidAmount;

@ExcelProperty("剩余金额（元）")
private BigDecimal remainingAmount;
```

### 测试验证点
- [ ] 能否识别注解值的变更
- [ ] 能否识别 @ExcelProperty 注解
- [ ] 能否识别单位变更（万元 → 元）
- [ ] 能否统计受影响的字段数量 (3个)

---

## 4️⃣ OrderDetailVO.java - 视图对象新增

### 变更类型
- **VO (视图对象)** - 新增类

### 变更内容
新增订单详情视图对象，包含 8 个字段：

```java
private String orderNo;           // 订单编号
private String orderName;         // 订单名称
private BigDecimal amount;        // 订单金额
private String orderStatus;       // 订单状态
private String approvalNode;      // 审批节点
private String handler;           // 经办人
private LocalDateTime createTime; // 创建时间
private LocalDateTime updateTime; // 更新时间
```

### 测试验证点
- [ ] 能否识别新增的 VO 类
- [ ] 能否识别类的用途（视图对象）
- [ ] 能否统计字段数量 (8个)
- [ ] 能否识别字段类型分布

---

## 5️⃣ OrderQueryPO.java - 持久化对象新增

### 变更类型
- **PO (持久化对象)** - 新增类

### 变更内容
新增订单查询持久化对象，包含 7 个字段：

```java
private String orderNo;           // 订单编号
private BigDecimal amount;        // 订单金额
private LocalDateTime payTime;    // 支付时间
private LocalDateTime completeTime; // 完成时间
private BigDecimal actualAmount;  // 实际支付金额
private BigDecimal feeAmount;     // 手续费
private String status;            // 订单状态
```

### 测试验证点
- [ ] 能否识别新增的 PO 类
- [ ] 能否识别类的用途（持久化对象）
- [ ] 能否统计字段数量 (7个)
- [ ] 能否识别 BigDecimal 类型字段

---

## 🎯 综合测试验证点

### 文件类型识别
- [ ] 能否正确识别 5 个数据模型类文件
- [ ] 能否区分 DO、DTO、VO、PO 类型
- [ ] 能否统计各类型数量

### 变更类型识别
- [ ] 能否识别字段新增变更 (2个文件)
- [ ] 能否识别注解修改变更 (1个文件)
- [ ] 能否识别新增类变更 (2个文件)

### 字段统计
- [ ] 能否统计总新增字段数量 (19个)
- [ ] 能否统计字段类型分布
  - Long: 2个
  - String: 11个
  - BigDecimal: 4个
  - LocalDateTime: 6个

### 风险评估
- [ ] 能否评估变更风险等级
  - OrderDO: 中风险（实体类字段新增）
  - OrderDTO: 中风险（DTO字段新增）
  - OrderExportDTO: 低风险（注解修改）
  - OrderDetailVO: 高风险（新增类）
  - OrderQueryPO: 低风险（新增类）

---

## 📊 预期分析结果

### 文件统计
```
数据模型类变更: 5个文件
- DO: 1个 (OrderDO.java)
- DTO: 2个 (OrderDTO.java, OrderExportDTO.java)
- VO: 1个 (OrderDetailVO.java)
- PO: 1个 (OrderQueryPO.java)
```

### 变更统计
```
字段新增: 19个字段
注解修改: 3个字段
新增类: 2个类
```

### 影响范围
```
可能影响的模块:
- Service 层（使用 DO/DTO）
- Mapper 层（使用 DO）
- Controller 层（使用 DTO/VO）
- 导出功能（使用 ExportDTO）
```

---

## 🔍 测试执行步骤

### 1. 提交变更到 Git
```bash
cd code_diff_project/workspace/service-a
git add .
git commit -m "Test: 数据模型类变更 - DO/DTO/VO/PO"
```

### 2. 运行分析程序
```bash
python code_diff_project/backend/analyzer/runner.py \
    --project service-a \
    --mode git \
    --branch HEAD~1..HEAD
```

### 3. 验证分析结果
检查分析报告中是否包含：
- 5个数据模型类文件的变更
- 正确的变更类型识别
- 准确的字段统计
- 合理的风险评估

### 4. 回滚变更（可选）
```bash
cd code_diff_project/workspace/service-a
git reset --hard HEAD~1
```

---

**创建时间**: 2026-01-30  
**测试类型**: 数据模型类 (DO/DTO/VO/PO)  
**参考**: Task #110 真实变更数据
