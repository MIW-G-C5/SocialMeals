package nl.miwgroningen.cohort5.socialmeals.service;

import nl.miwgroningen.cohort5.socialmeals.dto.RatingDTO;

/**
 * @author Wessel van Dommelen <w.r.van.dommelen@st.hanze.nl>
 * establishes the required functions for the connection to the Database for handling ratings.
 */
public interface RatingService {

    RatingDTO addNew(RatingDTO ratingDTO);

}
