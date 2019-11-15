package edu.palermo.lab.i.user.doctor;

import lombok.NonNull;

import java.util.List;
import java.util.Optional;

public interface DoctorMetadataDao {

  List<DoctorDto> getAll();
  void save(@NonNull final DoctorDto metadata);
  Optional<DoctorDto> getDoctorMetadata(@NonNull final String doctorId);

}
