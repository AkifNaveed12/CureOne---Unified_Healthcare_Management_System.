package com.cureone.appointmentsandscheduling.repository;

import com.cureone.appointmentsandscheduling.model.Appointment;
import com.cureone.common.DBUtil;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class JdbcAppointmentRepository implements AppointmentRepository {
    private final Connection conn;

    public JdbcAppointmentRepository() {
        this.conn = DBUtil.getConnection();
    }


    // ---------------- SAVE ----------------
    @Override
    public int save(Appointment appointment) {

        String sql = """
            INSERT INTO appointments
            (patient_id, doctor_id, appointment_date, appointment_time,
             duration_minutes, reason, status)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps =
                     c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, appointment.getPatientId());
            ps.setInt(2, appointment.getDoctorId());
            ps.setDate(3, Date.valueOf(appointment.getAppointmentDate()));

            // ✅ LocalTime → SQL Time (NO parsing)
            ps.setTime(4, Time.valueOf(appointment.getAppointmentTime()));

            ps.setInt(5, appointment.getDurationMinutes());
            ps.setString(6, appointment.getReason());
            ps.setString(7,
                    appointment.getStatus() == null
                            ? "SCHEDULED"
                            : appointment.getStatus()
            );

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    // ---------------- UPDATE ----------------
    @Override
    public boolean update(Appointment appointment) {

        String sql = """
            UPDATE appointments
            SET patient_id=?,
                doctor_id=?,
                appointment_date=?,
                appointment_time=?,
                duration_minutes=?,
                reason=?,
                status=?
            WHERE id=?
        """;

        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, appointment.getPatientId());
            ps.setInt(2, appointment.getDoctorId());
            ps.setDate(3, Date.valueOf(appointment.getAppointmentDate()));
            ps.setTime(4, Time.valueOf(appointment.getAppointmentTime()));
            ps.setInt(5, appointment.getDurationMinutes());
            ps.setString(6, appointment.getReason());
            ps.setString(7, appointment.getStatus());
            ps.setInt(8, appointment.getId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ---------------- DELETE ----------------
    @Override
    public boolean delete(int id) {

        String sql = "DELETE FROM appointments WHERE id=?";

        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ---------------- FIND BY ID ----------------
    @Override
    public Appointment findById(int id) {

        String sql = "SELECT * FROM appointments WHERE id=?";

        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // ---------------- FIND BY DOCTOR + DATE ----------------
    @Override
    public List<Appointment> findByDoctorAndDate(int doctorId, LocalDate date) {

        String sql = """
            SELECT * FROM appointments
            WHERE doctor_id = ?
              AND appointment_date = ?
            ORDER BY appointment_time
        """;

        List<Appointment> list = new ArrayList<>();

        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, doctorId);
            ps.setDate(2, Date.valueOf(date));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ---------------- EXISTS (TIME CLASH CHECK) ----------------
    @Override
    public boolean existsByDoctorAndTime(
            int doctorId, LocalDate date, LocalTime time) {

        String sql = """
            SELECT 1 FROM appointments
            WHERE doctor_id = ?
              AND appointment_date = ?
              AND appointment_time = ?
        """;

        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, doctorId);
            ps.setDate(2, Date.valueOf(date));
            ps.setTime(3, Time.valueOf(time));

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ---------------- MAPPER ----------------
    private Appointment mapRow(ResultSet rs) throws SQLException {

        Appointment a = new Appointment();

        a.setId(rs.getInt("id"));
        a.setPatientId(rs.getInt("patient_id"));
        a.setDoctorId(rs.getInt("doctor_id"));
        a.setAppointmentDate(
                rs.getDate("appointment_date").toLocalDate()
        );
        a.setAppointmentTime(
                rs.getTime("appointment_time").toLocalTime()
        );
        a.setDurationMinutes(rs.getInt("duration_minutes"));
        a.setReason(rs.getString("reason"));
        a.setStatus(rs.getString("status"));

        return a;
    }

    @Override
    public List<Appointment> findByPatient(int patientId) {

        String sql = """
        SELECT * FROM appointments
        WHERE patient_id = ?
        ORDER BY appointment_date, appointment_time
    """;

        List<Appointment> list = new ArrayList<>();

        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, patientId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public List<Appointment> findByPatientAndDate(int patientId, LocalDate date) {

        String sql = """
        SELECT * FROM appointments
        WHERE patient_id = ?
          AND appointment_date = ?
        ORDER BY appointment_time
    """;

        List<Appointment> list = new ArrayList<>();

        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, patientId);
            ps.setDate(2, Date.valueOf(date));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    @Override
    public List<Appointment> findAll() {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT * FROM appointments";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    @Override
    public List<Appointment> findByDoctor(int doctorId) {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT * FROM appointments WHERE doctor_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, doctorId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Appointment> findByDoctorOrPatient(int id) {

        List<Appointment> list = new ArrayList<>();

        String sql = """
        SELECT * FROM appointments
        WHERE doctor_id = ? OR patient_id = ?
        ORDER BY appointment_date, appointment_time
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.setInt(2, id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapRow(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }





}
