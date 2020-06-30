package com.firecode.computer.test.theoretical.parser.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 符号优先级表
 */
public class PriorityTable {
    private List<List<String>> table = new ArrayList<>();

    public PriorityTable() {
        table.add(Arrays.asList(new String[]{"&", "|", "^"}));
        table.add(Arrays.asList(new String[]{"==", "!=", ">", "<", ">=", "<="}));
        table.add(Arrays.asList(new String[]{"+", "-"}));
        table.add(Arrays.asList(new String[]{"*", "/"}));
        table.add(Arrays.asList(new String[]{"<<", ">>"}));
    }

    public int size(){
        return table.size();
    }

    /**
     * 根据级别获取该级别的符号
     * @param level
     * @return
     */
    public List<String> get(int level) {
        return table.get(level);
    }


}
