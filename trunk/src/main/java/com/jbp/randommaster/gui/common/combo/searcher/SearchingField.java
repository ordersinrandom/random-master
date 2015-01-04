package com.jbp.randommaster.gui.common.combo.searcher;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicComboBoxEditor;

@SuppressWarnings("rawtypes")
public class SearchingField extends JComboBox  {
  
	private static final long serialVersionUID = 7995765622216980193L;
	SearchingFieldEditor editor;
  
  @SuppressWarnings("unchecked")
  public SearchingField(SearchingModel model) {
    super(model);
    super.setEditable(true);
    model.setSearchingField(this);
    
    editor=new SearchingFieldEditor();
    setEditor(editor);
    
    ((JTextField) super.getEditor().getEditorComponent()).addActionListener(new FieldActionListener());

    // hide popup when the text field loss focus.
    ((JTextField) super.getEditor().getEditorComponent()).addFocusListener(new FocusAdapter() {
        public void focusLost(FocusEvent e) {
          if (isPopupVisible())
            hidePopup();
        }
      });
  }
  
  public void dispose() {
    SearchingModel m=(SearchingModel) super.getModel();
    m.dispose();
  }
  
  public String getTextFieldString() {
    JTextField f=(JTextField) editor.getEditorComponent();
    return f.getText();
  }
  
  public void showResults(String textInField) {
    if (super.isPopupVisible())
      hidePopup();
    
    // show popup only if the editor component has the focus
    if (super.getEditor().getEditorComponent().hasFocus())
      showPopup();
    // not in focus, check if only one result is available,
    // if yes... show it.
    else {
      SearchingModel m=(SearchingModel) SearchingField.this.getModel();
      int size=m.getSize();
      // if only one item, select it,
      if (size==1) 
        SearchingField.this.setSelectedIndex(0);
    }

    reportSearchSuccessful();
  }
  
  public void clearResults(String textInField) {
    if (super.isPopupVisible())
      hidePopup();
    
    super.setSelectedItem(textInField);
  }
  
  /**
   * Call this method to set the text field value, clean up and hide the combo box drop down list.
   * @param userInput
   */
  public void setNewUserInput(String userInput) {
    SearchingModel m=(SearchingModel) super.getModel();
    m.removeAll();
    // trigger to search
    m.userInputUpdated(userInput);
    // then select the item.
    super.setSelectedItem(userInput);
  }
  
  
  public void reportSearchSuccessful() {
    editor.getEditorComponent().setForeground(Color.black);
  }
  
  public void reportSearchFailed() {
    editor.getEditorComponent().setForeground(Color.red);
  }
  
  private class SearchingFieldEditor extends BasicComboBoxEditor {
    
    private String lastKnownInput;
    
    public SearchingFieldEditor() {
      super();
      
      super.getEditorComponent().addKeyListener(new KeyAdapter() {
          public void keyReleased(KeyEvent e) {
            String text=((JTextField) getEditorComponent()).getText();
            SearchingModel m=(SearchingModel) SearchingField.this.getModel();
            
            if (text.equals(lastKnownInput)) {
              // ignore
            }
            else {
              lastKnownInput=text;
              m.userInputUpdated(text);
            }   
          }
        });
    }
    
  }
  
  private class FieldActionListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      int selectedIndex=SearchingField.this.getSelectedIndex();
      
      if (selectedIndex<0) {
        
        SearchingModel m=(SearchingModel) SearchingField.this.getModel();
        int size=m.getSize();
        // if only one item, select it,
        if (size==1) {
          SearchingField.this.setSelectedIndex(0);
          reportSearchSuccessful();
        }
        // otherwise report failed.
        else {
          reportSearchFailed();
          // also trigger a search
          String text=(String) getSelectedItem();
          setNewUserInput(text);
        }
      }
      // if already selected something, report successful.
      else reportSearchSuccessful(); 
    }
    
  }
}