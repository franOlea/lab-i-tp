package edu.palermo.lab.i.user.doctor.persistence;

import edu.palermo.lab.i.user.doctor.DoctorDto;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;

public interface DoctorMetadataDao {

  List<DoctorDto> getAll();
  void save(@NonNull final DoctorDto metadata);
  Optional<DoctorDto> getDoctorMetadata(@NonNull final String doctorId);

}
