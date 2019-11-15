package edu.palermo.lab.i.user.ui.list;

import edu.palermo.lab.i.user.UserDto;
import lombok.RequiredArgsConstructor;

import javax.swing.table.AbstractTableModel;
import java.util.List;

@RequiredArgsConstructor
public class UserTableModel extends AbstractTableModel {

  private static final int ID_COL_INDEX = 0;
  private static final int FIRST_NAME_COL_INDEX = 1;
  private static final int LAST_NAME_COL_INDEX = 2;
  private static final int ROLE_COL_INDEX = 3;
  private static final int ENABLED_COL_INDEX = 4;

  private String[] columnHeaders = {"Id", "Nombre", "Apellido", "Rol", "Habilitado"};
  private Class[] columnTypes = {String.class, String.class, String.class, String.class, String.class};

  private final transient List<UserDto> users;

  @Override
  public int getRowCount() {
    return users.size();
  }

  @Override
  public int getColumnCount() {
    return columnHeaders.length;
  }

  @Override
  public Object getValueAt(final int rowIndex, final int columnIndex) {
    UserDto user = users.get(rowIndex);
    if(columnIndex == ID_COL_INDEX) {
      return user.getId();
    } else if(columnIndex == FIRST_NAME_COL_INDEX) {
      return user.getFirstName();
    } else if(columnIndex == LAST_NAME_COL_INDEX) {
      return user.getLastName();
    } else if(columnIndex == ROLE_COL_INDEX) {
      return user.getRole();
    } else if(columnIndex == ENABLED_COL_INDEX) {
      return user.getEnabled().toString();
    } else {
      throw new IllegalArgumentException(String.format("The column index is invalid [%s].", columnIndex));
    }
  }

  @Override
  public String getColumnName(final int column) {
    return columnHeaders[column];
  }

  @Override
  public Class<?> getColumnClass(final int columnIndex) {
    return columnTypes[columnIndex];
  }

}
