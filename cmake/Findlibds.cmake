
if (LIBDS_LIBRARY AND LIBDS_INCLUDE_DIR)
  # In cmake cache already
  set (LIBDS_FOUND TRUE)
else (LIBDS_LIBRARY AND LIBDS_INCLUDE_DIR)
  find_path(INCLUDE_DIR
    NAMES
      LibDS.h
    PATHS
      /usr/include
      /usr/local/include
      /opt/local/include
      /sw/include
      /home/tomtzook/git/LibDS/include
  )

  find_library(LIBRARY_DIR
    NAMES
      LibDS
    PATHS
      /usr/lib
      /usr/local/lib
      /opt/local/lib
      /sw/lib
      /home/tomtzook/git/LibDS
  )

  message ("lib ${LIBRARY_DIR}")
  message ("inc ${INCLUDE_DIR}")

  set (LIBDS_INCLUDE_DIR ${INCLUDE_DIR})
  set (LIBDS_LIBRARY ${LIBRARY_DIR})

  if (LIBDS_INCLUDE_DIR AND LIBDS_LIBRARY)
    set (LIBDS_FOUND TRUE)
  endif (LIBDS_INCLUDE_DIR AND LIBDS_LIBRARY)

  if (LIBDS_FOUND)
    message (STATUS "Found libds")
  else (LIBDS_FOUND)
    message (FATAL_ERROR "Could not find libds")
  endif (LIBDS_FOUND)

  mark_as_advanced (LIBDS_INCLUDE_DIR LIBDS_LIBRARY)

endif (LIBDS_LIBRARY AND LIBDS_INCLUDE_DIR)
