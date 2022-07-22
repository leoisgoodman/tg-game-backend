-- AI-Code 为您构建代码，享受智慧生活!
CREATE SCHEMA IF NOT EXISTS tggame;
USE tggame;

DROP TABLE IF EXISTS bet;
DROP TABLE IF EXISTS bet_order;
DROP TABLE IF EXISTS bot;
DROP TABLE IF EXISTS `group`;
DROP TABLE IF EXISTS group_bet;
DROP TABLE IF EXISTS group_lottery;
DROP TABLE IF EXISTS lottery;
DROP TABLE IF EXISTS open_record;
DROP TABLE IF EXISTS tg_group_bot;
DROP TABLE IF EXISTS tg_user_flow;
DROP TABLE IF EXISTS trend_record;
DROP TABLE IF EXISTS `user`;


CREATE TABLE bet
(
    id               varchar(64) NOT NULL comment 'id',
    lottery_id       varchar(64) comment '彩种id',
    name             varchar(32) comment '玩法名',
    code             varchar(32) NOT NULL comment '编码',
    min_bet_money    float comment '最小投注额',
    max_bet_money    float comment '最大投注额',
    odds             float comment '赔率',
    default_money    float comment '默认投注额，tg键盘显示用',
    type             varchar(16) comment '类型:两面 Both_Sides,号码 Num',
    pay_back_percent float comment '返奖率,如：98.5% 存储 98.5',
    status           varchar(16) comment '状态:启用 Enable，停用 Disable',
    summary          varchar(128) comment '说明',
    PRIMARY KEY (id, code),
    INDEX (lottery_id),
    INDEX (type),
    INDEX (status)
) comment ='玩法';
CREATE TABLE bet_order
(
    id                varchar(64) NOT NULL comment 'id',
    group_id          varchar(64) comment '群id',
    tg_group_id       varchar(64) comment 'tg群id',
    user_id           varchar(64) comment '用户id',
    tg_user_id        varchar(64) NOT NULL comment 'tg下用户id',
    bot_id            varchar(64) comment '机器人id',
    lottery_id        varchar(64) comment '彩种id',
    lottery_name      varchar(64) comment '彩种',
    bet_id            varchar(64) comment '玩法id',
    bet_name          varchar(32) comment '玩法名',
    bet_num           varchar(32) comment '投注数字',
    odds              float comment '赔率',
    bet_type          varchar(16) comment '投注类型:两面 Both_Sides,号码 Num',
    pay_back_percent  float comment '返奖率，如：97.5% 存储97.5',
    amount            double comment '投注金额',
    win_amount        double comment '中奖金额',
    should_pay_amount double comment '应派奖金额=投注金额×赔率×返奖率',
    status            varchar(16) comment '状态：投注 Bet，赢 Win，输 Lost，派彩中 Send_Money，已派彩  Sent_Money_Done，作废 Invalid',
    open_id           varchar(64) comment '开奖id',
    create_time       datetime    NULL comment '创建时间',
    update_time       datetime    NULL comment '更新时间',
    PRIMARY KEY (id),
    INDEX (group_id),
    INDEX (tg_group_id),
    INDEX (tg_user_id),
    INDEX (bot_id),
    INDEX (lottery_id),
    INDEX (bet_id),
    INDEX (bet_type),
    INDEX (open_id)
) comment ='投注';
CREATE TABLE bot
(
    id                 varchar(64) NOT NULL comment 'id',
    tg_bot_id          varchar(64) NOT NULL comment 'tg 机器人id',
    name               varchar(32) comment 'tg机器人名称',
    tg_token           varchar(128) comment 'tg授权机器人token,tg中获得',
    bot_owner_username varchar(32) comment '机器人创建的tg账号',
    tg_phone           varchar(11) comment '手机号',
    share_link         varchar(128) comment '机器人链接',
    status             varchar(16) comment '状态:启用 Enable,停用 Disable',
    create_time        datetime    NULL comment '创建时间',
    update_time        datetime    NULL comment '更新时间',
    PRIMARY KEY (id, tg_bot_id),
    INDEX (bot_owner_username),
    INDEX (status)
) comment ='tg机器人';
CREATE TABLE `group`
(
    id           varchar(64) NOT NULL comment 'id',
    merchant_id  varchar(64),
    name         varchar(64) comment '群名称',
    tg_group_id  varchar(64) NOT NULL comment 'tg群id',
    tg_game_code varchar(32) comment 'tg小游戏编码',
    domain       varchar(128) comment '群绑定的小游戏域名',
    status       varchar(16) comment '状态:启用 Enable,停用 Disable',
    create_time  datetime    NULL comment '创建时间',
    summary      varchar(128) comment '群说明',
    game_summary varchar(255) comment '群游戏欢迎说明',
    PRIMARY KEY (id, tg_group_id),
    INDEX (merchant_id),
    INDEX (tg_game_code),
    INDEX (domain),
    INDEX (status)
) comment ='tg群';
CREATE TABLE group_bet
(
    id               varchar(64) NOT NULL comment 'id',
    group_id         varchar(64) comment '群id',
    group_lottery_id varchar(64) comment '群彩种id',
    bet_id           varchar(64) comment '玩法id',
    code             varchar(32) NOT NULL comment '编码',
    min_bet_money    float comment '最小投注额',
    max_bet_money    float comment '最大投注额',
    odds             float comment '赔率',
    default_money    float comment '默认投注额，tg键盘显示用',
    type             varchar(16) comment '类型:两面 Both_Sides,号码 Num',
    pay_back_percent float comment '返奖率,如：98.5% 存储 98.5',
    status           varchar(16) comment '状态:启用 Enable，停用 Disable',
    PRIMARY KEY (id, code),
    INDEX (group_id),
    INDEX (group_lottery_id),
    INDEX (type),
    INDEX (status)
) comment ='群玩法';
CREATE TABLE group_lottery
(
    id         varchar(64) NOT NULL comment 'id',
    group_id   varchar(64) comment '群id',
    lottery_id varchar(64) comment '彩种id',
    PRIMARY KEY (id),
    INDEX (group_id),
    INDEX (lottery_id)
) comment ='群彩种';
CREATE TABLE lottery
(
    id      varchar(64) NOT NULL comment 'id',
    name    varchar(32) comment ' 名称',
    pre_id  varchar(64) comment '上级id',
    code    varchar(32) NOT NULL comment '编码',
    status  varchar(16) comment '状态:启用 Enable,停用 Disable',
    summary varchar(128) comment '说明',
    PRIMARY KEY (id, code),
    INDEX (status)
) comment ='彩种';
CREATE TABLE open_record
(
    id          varchar(64) NOT NULL comment 'id',
    lottery_id  varchar(64) comment '彩种id',
    issue       int(10) comment '期号',
    num         varchar(16) comment '开奖号码',
    status      varchar(16) comment '状态：就绪 Ready，可投注 Enable，封盘  Lock，已开奖 Drawn ,作废  Invalid',
    create_time datetime    NULL comment '创建时间',
    update_time datetime    NULL comment '更新时间',
    PRIMARY KEY (id),
    INDEX (lottery_id),
    INDEX (issue),
    INDEX (status)
) comment ='开奖记录';
CREATE TABLE tg_group_bot
(
    id       varchar(64) NOT NULL comment 'id',
    group_id varchar(64) comment '群id',
    bot_id   varchar(64) comment '机器人id',
    PRIMARY KEY (id),
    INDEX (group_id),
    INDEX (bot_id)
) comment ='群机器人';
CREATE TABLE tg_user_flow
(
    id          varchar(64) NOT NULL comment 'id',
    user_id     varchar(64) comment '用户id',
    tg_username varchar(32) comment 'tg下用户名',
    amount      double comment '金额',
    type        varchar(16) comment '类型: 充值 Recharge,投注支出 Bet_Paid,中奖收益 Bet_Win, 提现 Withdraw，投注退款 Bet_Refund',
    status      varchar(16) comment '状态: 审核中 Audit,已审核 Done',
    `from`      varchar(255) comment 'usdt发起者地址',
    `to`        varchar(255) comment 'usdt收款地址',
    create_time datetime    NULL comment '创建时间',
    update_time datetime    NULL comment '更新时间',
    summary     varchar(128) comment '说明',
    PRIMARY KEY (id),
    INDEX (user_id),
    INDEX (tg_username),
    INDEX (type),
    INDEX (status)
) comment ='用户流水';
CREATE TABLE trend_record
(
    id          varchar(64) NOT NULL comment 'id',
    lottery_id  varchar(64) comment '彩种id',
    issue       int(10) comment '期号',
    data        varchar(64) comment '走势数据',
    open_time   time(7) comment '开奖时间',
    create_time datetime    NULL comment '创建时间',
    PRIMARY KEY (id)
) comment ='走势记录';
CREATE TABLE `user`
(
    id              varchar(64) NOT NULL comment 'id',
    group_id        varchar(64) comment '群id',
    tg_user_id      varchar(64) NOT NULL comment 'tg用户id，来自tg',
    tg_username     varchar(64) NOT NULL comment 'tg下用户名',
    username        varchar(32) NOT NULL comment '本系统生成的用户名，默认和tg保持一致',
    password        varchar(32) comment '密码，默认和用户名一致',
    google_code     varchar(32) comment '谷歌口令',
    `percent`       int(10) comment '占成比例，如:10% 存储为10',
    status          varchar(16) comment '状态:可投注 Enable,禁止投注 Disable，庄审核 Verify_Banker',
    type            varchar(16) comment '类型:试玩 Demo,玩家 Player,庄家 Banker',
    is_owner        varchar(4) comment '是否群主：是 Y,否 N',
    is_admin        varchar(4) comment '是否管理员：是 Y，否 N',
    usdt_balance    double comment 'usdt 余额',
    usdt_address    varchar(255) comment 'usdt 地址',
    bet_total_money bigint(19) comment '投注总额',
    bet_win_money   bigint(19) comment '投注盈利',
    bet_lost_money  bigint(19) comment '投注亏损总额',
    summary         varchar(128) comment '说明',
    create_time     datetime    NULL comment '创建时间',
    update_time     datetime    NULL comment '更新时间',
    PRIMARY KEY (id, tg_user_id, tg_username, username),
    INDEX (group_id),
    INDEX (status),
    INDEX (type),
    INDEX (is_owner),
    INDEX (is_admin)
) comment ='群成员';

CREATE TABLE `worker_node`
(
    `ID`          bigint(20)  NOT NULL AUTO_INCREMENT COMMENT 'auto increment id',
    `HOST_NAME`   varchar(64) NOT NULL COMMENT 'host name',
    `PORT`        varchar(64) NOT NULL COMMENT 'port',
    `TYPE`        int(11)     NOT NULL COMMENT 'node type: ACTUAL or CONTAINER',
    `LAUNCH_DATE` date        NOT NULL COMMENT 'launch date',
    `MODIFIED`    timestamp   NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT 'modified time',
    `CREATED`     timestamp   NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT 'created time',
    PRIMARY KEY (`ID`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1263
  DEFAULT CHARSET = utf8 COMMENT ='DB WorkerID Assigner for UID Generator';
