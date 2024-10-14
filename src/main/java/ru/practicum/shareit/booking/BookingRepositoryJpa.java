package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface BookingRepositoryJpa extends JpaRepository<Booking, Long> {

    @Query(value = "select bk from Booking as bk " +
            "join bk.booker as bo " +
            "join bk.item as it " +
            "join it.owner as ow " +
            "where bo.id = :bookingId or ow.id = :userId")
    Booking approved(@Param("userId") long userId, @Param("bookingId") long bookingId);

    @Query(value = "select bk from Booking as bk join bk.booker as bo where bo.id = :userId ORDER BY bk.start desc")
    List<Booking> findAllBookingsById(@Param("userId") long userId);

    @Query(value = "select bk from Booking as bk join bk.booker as bo where bo.id = :userId and :instantNow between bk.start and bk.end ORDER BY bk.start desc")
    List<Booking> findCurrentBookingsById(@Param("userId") long userId, @Param("instantNow") Instant instantNow);

    @Query(value = "select bk from Booking as bk join bk.booker as bo where bo.id = :userId and :instantNow > bk.end ORDER BY bk.start desc")
    List<Booking> findPastBookingsById(@Param("userId") long userId, @Param("instantNow") Instant instantNow);

    @Query(value = "select bk from Booking as bk join bk.booker as bo where bo.id = :userId and :instantNow < bk.start ORDER BY bk.start desc")
    List<Booking> findFutureBookingsById(@Param("userId") long userId, @Param("instantNow") Instant instantNow);

    @Query(value = "select bk from Booking as bk join bk.booker as bo where bo.id = :userId and bk.status = WAITING ORDER BY bk.start desc")
    List<Booking> findWaitingBookingsById(@Param("userId") long userId);

    @Query(value = "select bk from Booking as bk join bk.booker as bo where bo.id = :userId and bk.status = REJECTED ORDER BY bk.start desc")
    List<Booking> findRejectedBookingsById(@Param("userId") long userId);

    @Query(value = "select count(it) from Item as it join it.owner as ow where ow.id = :userId")
    Long countItemsUser(@Param("userId") long userId);

    @Query(value = "select bk from Booking as bk " +
            "join bk.booker as bo " +
            "join bk.item as it " +
            "join it.owner as ow " +
            "where ow.id = :userId ORDER BY bk.start desc")
    List<Booking> findAllBookingsForItemsById(@Param("userId") long userId);

    @Query(value = "select bk from Booking as bk " +
            "join bk.booker as bo " +
            "join bk.item as it " +
            "join it.owner as ow " +
            "where ow.id = :userId " +
            "and :instantNow between bk.start and bk.end " +
            "ORDER BY bk.start desc")
    List<Booking> findCurrentBookingsForItemsById(@Param("userId") long userId, @Param("instantNow") Instant instantNow);

    @Query(value = "select bk from Booking as bk " +
            "join bk.booker as bo " +
            "join bk.item as it " +
            "join it.owner as ow " +
            "where ow.id = :userId " +
            "and :instantNow > bk.end " +
            "ORDER BY bk.start desc")
    List<Booking> findPastBookingsForItemsById(@Param("userId") long userId, @Param("instantNow") Instant instantNow);

    @Query(value = "select bk from Booking as bk " +
            "join bk.booker as bo " +
            "join bk.item as it " +
            "join it.owner as ow " +
            "where ow.id = :userId " +
            "and :instantNow < bk.start " +
            "ORDER BY bk.start desc")
    List<Booking> findFutureBookingsForItemsById(@Param("userId") long userId, @Param("instantNow") Instant instantNow);

    @Query(value = "select bk from Booking as bk " +
            "join bk.booker as bo " +
            "join bk.item as it " +
            "join it.owner as ow " +
            "where ow.id = :userId " +
            "and bk.status = WAITING " +
            "ORDER BY bk.start desc")
    List<Booking> findWaitingBookingsForItemsById(@Param("userId") long userId);

    @Query(value = "select bk from Booking as bk " +
            "join bk.booker as bo " +
            "join bk.item as it " +
            "join it.owner as ow " +
            "where ow.id = :userId " +
            "and bk.status = REJECTED " +
            "ORDER BY bk.start desc")
    List<Booking> findRejectedBookingsForItemsById(@Param("userId") long userId);

    @Query(value = "select bk.start from Booking as bk " +
            "join bk.item as it " +
            "where it.id = :itemId " +
            "and :instantNow < bk.start " +
            "ORDER BY bk.start asc limit 1")
    Instant findNearestBookingsForItem(@Param("itemId") long itemId, @Param("instantNow") Instant instantNow);

    @Query(value = "select bk.start from Booking as bk " +
            "join bk.item as it " +
            "where it.id = :itemId " +
            "and :instantNow > bk.start " +
            "ORDER BY bk.start desc limit 1")
    Instant findLastBookingsForItem(@Param("itemId") long itemId, @Param("instantNow") Instant instantNow);

    @Query(value = "select count (bk) from Booking as bk " +
            "join bk.item as it " +
            "join bk.booker as bo " +
            "where it.id = :itemId " +
            "and bo.id = :userId " +
            "and :instantNow > bk.end ")
    long checkForAddComment(@Param("itemId") long itemId, @Param("userId") long userId, @Param("instantNow") Instant instantNow);

    @Query(value = "select count (bk) from Booking as bk " +
            "join bk.item as it " +
            "where it.id = :itemId " +
            "and bk.start < :end AND bk.end > :start")
    long checkIntersection(@Param("itemId") long itemId, @Param("start") Instant start, @Param("end") Instant end);
}
