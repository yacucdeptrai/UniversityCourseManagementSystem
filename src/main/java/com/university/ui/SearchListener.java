package main.java.com.university.ui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;

public class SearchListener implements DocumentListener {
    private JTextField searchField;
    private TableRowSorter<?> sorter;

    public SearchListener(JTextField searchField, TableRowSorter<?> sorter) {
        this.searchField = searchField;
        this.sorter = sorter;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        updateFilter();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        updateFilter();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        updateFilter();
    }

    private void updateFilter() {
        String text = searchField.getText().trim();
        if (text.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text)); // (?i) để không phân biệt chữ hoa/chữ thường
        }
    }
}
