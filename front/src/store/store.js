export const registerCourse = (course) => ({
  type: "REGISTER_COURSE",
  payload: course,
});
export const resetCourse = () => ({ type: "RESET_COURSE" });
export const setPlaces = (places) => ({
  type: "SET_PLACES",
  payload: places,
});
export const setMap = (map) => ({ type: "SET_MAP", payload: map });
export const setMarker = (marker) => ({ type: "SET_MARKER", payload: marker });
export const clearMarker = () => ({ type: "CLEAR_MARKERS" });

const initstate = {
  course: "hi",
  places: null,
  marker: null,
  map: null,
};

const reducer = (state = initstate, action) => {
  switch (action.type) {
    case "REGISTER_COURSE":
      // return action.course;
      return [...state.course, action.payload];
    case "RESET_COURSE":
      return [];
    case "SET_PLACES":
      return [...state.places, action.payload];
    case "SET_MAP":
      return state.map;
    case "SET_MARKER":
      return [...state.marker, action.payload];
    case "CLEAR_MARKERS":
      for (let i of state.marker) {
        i = null;
      }
      return [];
    default:
      return state;
  }
};

export default reducer;
