#ifndef KONAN_LIBDATACHANNEL_NATIVE_H
#define KONAN_LIBDATACHANNEL_NATIVE_H
#ifdef __cplusplus
extern "C" {
#endif
#ifdef __cplusplus
typedef bool            libdatachannel_native_KBoolean;
#else
typedef _Bool           libdatachannel_native_KBoolean;
#endif
typedef unsigned short     libdatachannel_native_KChar;
typedef signed char        libdatachannel_native_KByte;
typedef short              libdatachannel_native_KShort;
typedef int                libdatachannel_native_KInt;
typedef long long          libdatachannel_native_KLong;
typedef unsigned char      libdatachannel_native_KUByte;
typedef unsigned short     libdatachannel_native_KUShort;
typedef unsigned int       libdatachannel_native_KUInt;
typedef unsigned long long libdatachannel_native_KULong;
typedef float              libdatachannel_native_KFloat;
typedef double             libdatachannel_native_KDouble;
typedef float __attribute__ ((__vector_size__ (16))) libdatachannel_native_KVector128;
typedef void*              libdatachannel_native_KNativePtr;
struct libdatachannel_native_KType;
typedef struct libdatachannel_native_KType libdatachannel_native_KType;

typedef struct {
  libdatachannel_native_KNativePtr pinned;
} libdatachannel_native_kref_kotlin_Byte;
typedef struct {
  libdatachannel_native_KNativePtr pinned;
} libdatachannel_native_kref_kotlin_Short;
typedef struct {
  libdatachannel_native_KNativePtr pinned;
} libdatachannel_native_kref_kotlin_Int;
typedef struct {
  libdatachannel_native_KNativePtr pinned;
} libdatachannel_native_kref_kotlin_Long;
typedef struct {
  libdatachannel_native_KNativePtr pinned;
} libdatachannel_native_kref_kotlin_Float;
typedef struct {
  libdatachannel_native_KNativePtr pinned;
} libdatachannel_native_kref_kotlin_Double;
typedef struct {
  libdatachannel_native_KNativePtr pinned;
} libdatachannel_native_kref_kotlin_Char;
typedef struct {
  libdatachannel_native_KNativePtr pinned;
} libdatachannel_native_kref_kotlin_Boolean;
typedef struct {
  libdatachannel_native_KNativePtr pinned;
} libdatachannel_native_kref_kotlin_Unit;
typedef struct {
  libdatachannel_native_KNativePtr pinned;
} libdatachannel_native_kref_kotlin_UByte;
typedef struct {
  libdatachannel_native_KNativePtr pinned;
} libdatachannel_native_kref_kotlin_UShort;
typedef struct {
  libdatachannel_native_KNativePtr pinned;
} libdatachannel_native_kref_kotlin_UInt;
typedef struct {
  libdatachannel_native_KNativePtr pinned;
} libdatachannel_native_kref_kotlin_ULong;


typedef struct {
  /* Service functions. */
  void (*DisposeStablePointer)(libdatachannel_native_KNativePtr ptr);
  void (*DisposeString)(const char* string);
  libdatachannel_native_KBoolean (*IsInstance)(libdatachannel_native_KNativePtr ref, const libdatachannel_native_KType* type);
  libdatachannel_native_kref_kotlin_Byte (*createNullableByte)(libdatachannel_native_KByte);
  libdatachannel_native_KByte (*getNonNullValueOfByte)(libdatachannel_native_kref_kotlin_Byte);
  libdatachannel_native_kref_kotlin_Short (*createNullableShort)(libdatachannel_native_KShort);
  libdatachannel_native_KShort (*getNonNullValueOfShort)(libdatachannel_native_kref_kotlin_Short);
  libdatachannel_native_kref_kotlin_Int (*createNullableInt)(libdatachannel_native_KInt);
  libdatachannel_native_KInt (*getNonNullValueOfInt)(libdatachannel_native_kref_kotlin_Int);
  libdatachannel_native_kref_kotlin_Long (*createNullableLong)(libdatachannel_native_KLong);
  libdatachannel_native_KLong (*getNonNullValueOfLong)(libdatachannel_native_kref_kotlin_Long);
  libdatachannel_native_kref_kotlin_Float (*createNullableFloat)(libdatachannel_native_KFloat);
  libdatachannel_native_KFloat (*getNonNullValueOfFloat)(libdatachannel_native_kref_kotlin_Float);
  libdatachannel_native_kref_kotlin_Double (*createNullableDouble)(libdatachannel_native_KDouble);
  libdatachannel_native_KDouble (*getNonNullValueOfDouble)(libdatachannel_native_kref_kotlin_Double);
  libdatachannel_native_kref_kotlin_Char (*createNullableChar)(libdatachannel_native_KChar);
  libdatachannel_native_KChar (*getNonNullValueOfChar)(libdatachannel_native_kref_kotlin_Char);
  libdatachannel_native_kref_kotlin_Boolean (*createNullableBoolean)(libdatachannel_native_KBoolean);
  libdatachannel_native_KBoolean (*getNonNullValueOfBoolean)(libdatachannel_native_kref_kotlin_Boolean);
  libdatachannel_native_kref_kotlin_Unit (*createNullableUnit)(void);
  libdatachannel_native_kref_kotlin_UByte (*createNullableUByte)(libdatachannel_native_KUByte);
  libdatachannel_native_KUByte (*getNonNullValueOfUByte)(libdatachannel_native_kref_kotlin_UByte);
  libdatachannel_native_kref_kotlin_UShort (*createNullableUShort)(libdatachannel_native_KUShort);
  libdatachannel_native_KUShort (*getNonNullValueOfUShort)(libdatachannel_native_kref_kotlin_UShort);
  libdatachannel_native_kref_kotlin_UInt (*createNullableUInt)(libdatachannel_native_KUInt);
  libdatachannel_native_KUInt (*getNonNullValueOfUInt)(libdatachannel_native_kref_kotlin_UInt);
  libdatachannel_native_kref_kotlin_ULong (*createNullableULong)(libdatachannel_native_KULong);
  libdatachannel_native_KULong (*getNonNullValueOfULong)(libdatachannel_native_kref_kotlin_ULong);

  /* User functions. */
} libdatachannel_native_ExportedSymbols;
extern libdatachannel_native_ExportedSymbols* libdatachannel_native_symbols(void);
#ifdef __cplusplus
}  /* extern "C" */
#endif
#endif  /* KONAN_LIBDATACHANNEL_NATIVE_H */
