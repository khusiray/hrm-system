// File: com/hrm/rmi/client/ui/FamilyDetailsPanel.java
package com.hrm.rmi.client.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import com.hrm.rmi.client.ClientUtils;
import com.hrm.rmi.common.model.FamilyDetail;

public class FamilyDetailsPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private JComboBox<String> relationTypeCombo;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField contactNumberField;
    
    private JTable familyTable;
    private DefaultTableModel tableModel;
    
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton clearButton;
    private JButton refreshButton;
    
    private List<FamilyDetail> familyDetails;
    private FamilyDetail selectedDetail;
    
    private static final String[] RELATION_TYPES = {
        "Spouse", "Father", "Mother", "Son", "Daughter", "Brother", "Sister", "Other"
    };
    
    private static final String[] TABLE_COLUMNS = {
        "ID", "Relation Type", "First Name", "Last Name", "Contact Number"
    };
    
    public FamilyDetailsPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        initComponents();
        loadFamilyDetails();
    }
    
    private void initComponents() {
        // Title panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Family Details");
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);
        
        // Main panel (split into form and table)
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        
        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(new TitledBorder("Add/Update Family Member"));
        
        formPanel.add(new JLabel("Relation Type:"));
        relationTypeCombo = new JComboBox<>(RELATION_TYPES);
        formPanel.add(relationTypeCombo);
        
        formPanel.add(new JLabel("First Name:"));
        firstNameField = new JTextField();
        formPanel.add(firstNameField);
        
        formPanel.add(new JLabel("Last Name:"));
        lastNameField = new JTextField();
        formPanel.add(lastNameField);
        
        formPanel.add(new JLabel("Contact Number:"));
        contactNumberField = new JTextField();
        formPanel.add(contactNumberField);
        
        // Button panel (within form panel)
        JPanel formButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
        addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addFamilyDetail();
            }
        });
        
        updateButton = new JButton("Update");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateFamilyDetail();
            }
        });
        
        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteFamilyDetail();
            }
        });
        
        clearButton = new JButton("Clear");
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });
        
        formButtonPanel.add(addButton);
        formButtonPanel.add(updateButton);
        formButtonPanel.add(deleteButton);
        formButtonPanel.add(clearButton);
        
        formPanel.add(new JLabel("")); // Empty label for spacing
        formPanel.add(formButtonPanel);
        
        mainPanel.add(formPanel, BorderLayout.NORTH);
        
        // Table panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(new TitledBorder("Family Members"));
        
        tableModel = new DefaultTableModel(TABLE_COLUMNS, 0) {
            private static final long serialVersionUID = 1L;
            
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
        };
        
        familyTable = new JTable(tableModel);
        familyTable.getTableHeader().setReorderingAllowed(false);
        familyTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && familyTable.getSelectedRow() != -1) {
                int selectedRow = familyTable.getSelectedRow();
                selectFamilyDetail(selectedRow);
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(familyTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Refresh button panel
        JPanel refreshPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadFamilyDetails();
            }
        });
        refreshPanel.add(refreshButton);
        tablePanel.add(refreshPanel, BorderLayout.SOUTH);
        
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private void loadFamilyDetails() {
        try {
            int employeeId = ClientUtils.getCurrentEmployeeId();
            
            if (employeeId > 0) {
                familyDetails = ClientUtils.getEmployeeService().getFamilyDetailsByEmployeeId(employeeId);
                
                // Clear table
                tableModel.setRowCount(0);
                
                // Add data to table
                for (FamilyDetail detail : familyDetails) {
                    Object[] rowData = {
                        detail.getFamilyId(),
                        detail.getRelationType(),
                        detail.getFirstName(),
                        detail.getLastName(),
                        detail.getContactNumber()
                    };
                    tableModel.addRow(rowData);
                }
                
                // Clear form
                clearForm();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading family details: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void selectFamilyDetail(int selectedRow) {
        if (selectedRow >= 0 && selectedRow < familyDetails.size()) {
            selectedDetail = familyDetails.get(selectedRow);
            
            // Populate form fields
            for (int i = 0; i < RELATION_TYPES.length; i++) {
                if (RELATION_TYPES[i].equals(selectedDetail.getRelationType())) {
                    relationTypeCombo.setSelectedIndex(i);
                    break;
                }
            }
            
            firstNameField.setText(selectedDetail.getFirstName());
            lastNameField.setText(selectedDetail.getLastName());
            contactNumberField.setText(selectedDetail.getContactNumber() != null ? 
                    selectedDetail.getContactNumber() : "");
        }
    }
    
    private void addFamilyDetail() {
        try {
            // Validate fields
            String relationType = (String) relationTypeCombo.getSelectedItem();
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            
            if (firstName.isEmpty() || lastName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "First name and last name are required.", 
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Create new family detail
            FamilyDetail newDetail = new FamilyDetail();
            newDetail.setEmployeeId(ClientUtils.getCurrentEmployeeId());
            newDetail.setRelationType(relationType);
            newDetail.setFirstName(firstName);
            newDetail.setLastName(lastName);
            newDetail.setContactNumber(contactNumberField.getText().trim());
            
            // Save to server
            boolean success = ClientUtils.getEmployeeService().addFamilyDetail(newDetail);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Family member added successfully.", 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                
                // Refresh data
                loadFamilyDetails();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add family member.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error adding family member: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void updateFamilyDetail() {
        try {
            if (selectedDetail == null) {
                JOptionPane.showMessageDialog(this, "Please select a family member to update.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Validate fields
            String relationType = (String) relationTypeCombo.getSelectedItem();
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            
            if (firstName.isEmpty() || lastName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "First name and last name are required.", 
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Update detail
            selectedDetail.setRelationType(relationType);
            selectedDetail.setFirstName(firstName);
            selectedDetail.setLastName(lastName);
            selectedDetail.setContactNumber(contactNumberField.getText().trim());
            
            // Save to server
            boolean success = ClientUtils.getEmployeeService().updateFamilyDetail(selectedDetail);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Family member updated successfully.", 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                
                // Refresh data
                loadFamilyDetails();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update family member.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating family member: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void deleteFamilyDetail() {
        try {
            if (selectedDetail == null) {
                JOptionPane.showMessageDialog(this, "Please select a family member to delete.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Confirm deletion
            int confirmation = JOptionPane.showConfirmDialog(this, 
                    "Are you sure you want to delete this family member?", 
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
            
            if (confirmation != JOptionPane.YES_OPTION) {
                return;
            }
            
            // Delete from server
            boolean success = ClientUtils.getEmployeeService().deleteFamilyDetail(selectedDetail.getFamilyId());
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Family member deleted successfully.", 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                
                // Refresh data
                loadFamilyDetails();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete family member.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error deleting family member: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void clearForm() {
        selectedDetail = null;
        relationTypeCombo.setSelectedIndex(0);
        firstNameField.setText("");
        lastNameField.setText("");
        contactNumberField.setText("");
        familyTable.clearSelection();
    }
}