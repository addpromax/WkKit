package cn.wekyjay.www.wkkit.mysql.cdksqldata;

enum SQLCommand {
    // 创建表
    CREATE_TABLE(
            "CREATE TABLE IF NOT EXISTS `{table}` (" +
                    "`id` INT UNSIGNED AUTO_INCREMENT," +
                    "`cdk` VARCHAR(20) NOT NULL ," +
                    "`kits` VARCHAR(100) NOT NULL," +
                    "`date` VARCHAR(50) NOT NULL," +
                    "`status` VARCHAR(50) NOT NULL," +
                    "`mark` VARCHAR(100) NOT NULL," +
                    "PRIMARY KEY (`id`)," +
                    "UNIQUE KEY (`cdk`, `mark`)" +
                    ") DEFAULT CHARSET=utf8"
    ),


    // 添加数据
    ADD_DATA(
            "INSERT INTO `{table}` " +
                    "(`id`,`cdk`,`kits`,`date`,`status`,`mark`)" +
                    "VALUES (?, ?, ?, ?, ?, ?)"
    ),

    // 更新领取状态
    UPDATE_STATUS_DATA(
            "UPDATE `{table}` SET `status` = ? WHERE `cdk` = ? "
    ),

    // 更新Mark
    UPDATE_MARK_DATA(
            "UPDATE `{table}` SET `mark` = ? WHERE `mark` = ? "
    ),

    // 删除数据
    DELETE_DATA(
            "DELETE FROM `{table}` WHERE `cdk` = ?"
    ),
    // 查找CDK
    SELECT_MARK(
            "SELECT * FROM `{table}` WHERE `mark` = ?"
    ),
    // 查找CDK
    SELECT_CDK(
            "SELECT * FROM `{table}` WHERE `cdk` = ?"
    );


    private String command;

    SQLCommand(String command) {
        this.command = command;
    }

    public String commandToString() {
        return command;
    }

    public String format(String prefix) {
        return command.replace("{table}", prefix + "cdk");
    }
}
