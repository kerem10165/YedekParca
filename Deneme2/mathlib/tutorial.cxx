#include <iostream>
#include <cmath>
#include "TutorialConfig.h"
#ifdef USE_MYMATH
    #include "MathFunctions.h"
#endif

using namespace std;

#ifdef USE_MYMATH
  const double outputValue = mysqrt(25);
#else
  const double outputValue = sqrt(25);
#endif

int main()
{
    
}