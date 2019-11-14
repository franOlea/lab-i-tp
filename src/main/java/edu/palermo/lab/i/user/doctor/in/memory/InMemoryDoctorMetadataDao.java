package edu.palermo.lab.i.user.doctor.in.memory;

import edu.palermo.lab.i.user.doctor.DoctorMetadataDto;
import edu.palermo.lab.i.user.doctor.DoctorMetadataDao;
import lombok.NonNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class InMemoryDoctorMetadataDao implements DoctorMetadataDao {

  private final List<DoctorMetadataDto> doctors = new LinkedList<>();

  @Override
  public List<DoctorMetadataDto> getAll() {
    return doctors.stream()
        .map(DoctorMetadataDto::copy)
        .collect(Collectors.toList());
  }

  @Override
  public void save(@NonNull final DoctorMetadataDto metadata) {
    Optional<DoctorMetadataDto> potentialExistingDoctor = doctors.stream()
        .filter(savedDoctor -> savedDoctor.getUserId().equals(metadata.getUserId()))
        .findFirst();
    if(potentialExistingDoctor.isPresent()) {
      DoctorMetadataDto savedDoctor = potentialExistingDoctor.get();
      savedDoctor.setHourlyFee(metadata.getHourlyFee());
    } else {
      doctors.add(metadata);
    }
  }

  @Override
  public Optional<DoctorMetadataDto> getDoctorMetadata(@NonNull final String doctorId) {
    return doctors.stream()
        .filter(savedDoctor -> savedDoctor.getUserId().equals(doctorId))
        .findFirst()
        .map(DoctorMetadataDto::copy);
  }

}
