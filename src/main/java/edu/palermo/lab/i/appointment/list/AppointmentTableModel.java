package edu.palermo.lab.i.appointment.list;

import edu.palermo.lab.i.appointment.AppointmentDto;
import edu.palermo.lab.i.user.persistence.UserDao;
import lombok.RequiredArgsConstructor;

import javax.swing.table.AbstractTableModel;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;

@RequiredArgsConstructor
public class AppointmentTableModel extends AbstractTableModel {

  private static final int DATE_COL_INDEX = 0;
  private static final int TIME_COL_INDEX = 1;
  private static final int DOCTOR_COL_INDEX = 2;

  private String[] columnHeaders = {"Fecha", "Hora", "Doctor"};
  private Class[] columnTypes = {String.class, String.class, String.class};

  private final transient List<AppointmentDto> appointments;
  private final transient UserDao userDao;

  @Override
  public int getRowCount() {
    return appointments.size();
  }

  @Override
  public int getColumnCount() {
    return columnHeaders.length;
  }

  @Override
  public Object getValueAt(final int rowIndex, final int columnIndex) {
    AppointmentDto appointmentDto = appointments.get(rowIndex);
    LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(appointmentDto.getTimestamp()), ZoneOffset.UTC);
    if(columnIndex == DATE_COL_INDEX) {
      return localDateTime.toLocalDate();
    } else if(columnIndex == TIME_COL_INDEX) {
      return localDateTime.toLocalTime();
    } else if(columnIndex == DOCTOR_COL_INDEX) {
      return userDao.getById(appointmentDto.getDoctorId())
          .map(doctor -> doctor.getLastName() + ", " + doctor.getFirstName())
          .orElse("");
    } else {
      return "";
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
