package org.ohdsi.webapi.util;

import org.apache.commons.lang3.StringUtils;
import org.ohdsi.circe.cohortdefinition.*;

import java.util.Objects;

public class CohortUtils {

  public static String removeAllNonDigits(String dateString) {

    return StringUtils.isNotBlank(dateString) ? dateString.replaceAll("[^\\d.]", "") : dateString;
  }

  public static void formatCustomDates(CohortExpression cohortExpression) {

    formatCustomDates(cohortExpression.primaryCriteria.criteriaList);
    formatCustomDates(cohortExpression.censoringCriteria);
    for(InclusionRule rule : cohortExpression.inclusionRules) {
      formatCustomDates(rule.expression.criteriaList);
      formatCustomDates(rule.expression.groups);
    }
  }

  private static void formatCustomDates(CriteriaGroup[] groupList) {

    if (Objects.nonNull(groupList)) {
      for (CriteriaGroup group : groupList) {
        formatCustomDates(group.criteriaList);
        formatCustomDates(group.groups);
      }
    }
  }

  private static void formatCustomDates(CorelatedCriteria[] criteriaList) {

    if (Objects.nonNull(criteriaList)) {
      for (CorelatedCriteria criteria : criteriaList) {
        formatCustomDates(new Criteria[]{criteria.criteria});
      }
    }
  }

  private static void formatCustomDates(Criteria[] criteriaList) {

    if (Objects.nonNull(criteriaList)) {
      for (Criteria criteria : criteriaList) {
        if (criteria instanceof ObservationPeriod) {
          ObservationPeriod observationPeriod = (ObservationPeriod) criteria;
          if (Objects.nonNull(observationPeriod.userDefinedPeriod)) {
            observationPeriod.userDefinedPeriod.startDate = removeAllNonDigits(observationPeriod.userDefinedPeriod.startDate);
            observationPeriod.userDefinedPeriod.endDate = removeAllNonDigits(observationPeriod.userDefinedPeriod.endDate);
          }
        } else if (criteria instanceof PayerPlanPeriod) {
          PayerPlanPeriod payerPlanPeriod = (PayerPlanPeriod) criteria;
          if (Objects.nonNull(payerPlanPeriod.userDefinedPeriod)) {
            payerPlanPeriod.userDefinedPeriod.startDate = removeAllNonDigits(payerPlanPeriod.userDefinedPeriod.startDate);
            payerPlanPeriod.userDefinedPeriod.endDate = removeAllNonDigits(payerPlanPeriod.userDefinedPeriod.endDate);
          }
        }
      }
    }
  }
}
