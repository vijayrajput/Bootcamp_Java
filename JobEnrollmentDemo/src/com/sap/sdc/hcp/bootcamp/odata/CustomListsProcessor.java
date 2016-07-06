package com.sap.sdc.hcp.bootcamp.odata;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.olingo.odata2.annotation.processor.core.ListsProcessor;
import org.apache.olingo.odata2.annotation.processor.core.datasource.DataSource;
import org.apache.olingo.odata2.annotation.processor.core.datasource.ValueAccess;
import org.apache.olingo.odata2.api.ODataCallback;
import org.apache.olingo.odata2.api.batch.BatchHandler;
import org.apache.olingo.odata2.api.batch.BatchRequestPart;
import org.apache.olingo.odata2.api.batch.BatchResponsePart;
import org.apache.olingo.odata2.api.commons.HttpStatusCodes;
import org.apache.olingo.odata2.api.commons.InlineCount;
import org.apache.olingo.odata2.api.edm.EdmEntitySet;
import org.apache.olingo.odata2.api.edm.EdmEntityType;
import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.edm.EdmFunctionImport;
import org.apache.olingo.odata2.api.edm.EdmLiteral;
import org.apache.olingo.odata2.api.edm.EdmLiteralKind;
import org.apache.olingo.odata2.api.edm.EdmMapping;
import org.apache.olingo.odata2.api.edm.EdmProperty;
import org.apache.olingo.odata2.api.edm.EdmSimpleType;
import org.apache.olingo.odata2.api.edm.EdmSimpleTypeException;
import org.apache.olingo.odata2.api.edm.EdmSimpleTypeKind;
import org.apache.olingo.odata2.api.edm.EdmStructuralType;
import org.apache.olingo.odata2.api.edm.EdmTypeKind;
import org.apache.olingo.odata2.api.edm.EdmTyped;
import org.apache.olingo.odata2.api.ep.EntityProvider;
import org.apache.olingo.odata2.api.ep.EntityProviderBatchProperties;
import org.apache.olingo.odata2.api.ep.EntityProviderWriteProperties;
import org.apache.olingo.odata2.api.ep.callback.OnWriteEntryContent;
import org.apache.olingo.odata2.api.ep.callback.OnWriteFeedContent;
import org.apache.olingo.odata2.api.ep.callback.TombstoneCallback;
import org.apache.olingo.odata2.api.ep.callback.WriteCallbackContext;
import org.apache.olingo.odata2.api.ep.callback.WriteEntryCallbackContext;
import org.apache.olingo.odata2.api.ep.callback.WriteEntryCallbackResult;
import org.apache.olingo.odata2.api.ep.callback.WriteFeedCallbackContext;
import org.apache.olingo.odata2.api.ep.callback.WriteFeedCallbackResult;
import org.apache.olingo.odata2.api.exception.ODataApplicationException;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.apache.olingo.odata2.api.exception.ODataNotFoundException;
import org.apache.olingo.odata2.api.exception.ODataNotImplementedException;
import org.apache.olingo.odata2.api.processor.ODataContext;
import org.apache.olingo.odata2.api.processor.ODataRequest;
import org.apache.olingo.odata2.api.processor.ODataResponse;
import org.apache.olingo.odata2.api.uri.KeyPredicate;
import org.apache.olingo.odata2.api.uri.NavigationSegment;
import org.apache.olingo.odata2.api.uri.PathInfo;
import org.apache.olingo.odata2.api.uri.UriParser;
import org.apache.olingo.odata2.api.uri.expression.BinaryExpression;
import org.apache.olingo.odata2.api.uri.expression.CommonExpression;
import org.apache.olingo.odata2.api.uri.expression.ExpressionKind;
import org.apache.olingo.odata2.api.uri.expression.FilterExpression;
import org.apache.olingo.odata2.api.uri.expression.LiteralExpression;
import org.apache.olingo.odata2.api.uri.expression.MemberExpression;
import org.apache.olingo.odata2.api.uri.expression.MethodExpression;
import org.apache.olingo.odata2.api.uri.expression.OrderByExpression;
import org.apache.olingo.odata2.api.uri.expression.OrderExpression;
import org.apache.olingo.odata2.api.uri.expression.PropertyExpression;
import org.apache.olingo.odata2.api.uri.expression.SortOrder;
import org.apache.olingo.odata2.api.uri.expression.UnaryExpression;
import org.apache.olingo.odata2.api.uri.info.GetEntitySetUriInfo;

public class CustomListsProcessor extends ListsProcessor {
	  // TODO: Paging size should be configurable.
	  private static final int SERVER_PAGING_SIZE = 100;
	  
	public CustomListsProcessor(DataSource dataSource, ValueAccess valueAccess) {
		super(dataSource, valueAccess);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.apache.olingo.odata2.annotation.processor.core.ListsProcessor#readEntitySet(org.apache.olingo.odata2.api.uri.info.GetEntitySetUriInfo, java.lang.String)
	 */
	@Override
	public ODataResponse readEntitySet(GetEntitySetUriInfo uriInfo, String contentType) throws ODataException {
		// TODO Auto-generated method stub
		//return super.readEntitySet(uriInfo, contentType);
		ArrayList<Object> data = new ArrayList<Object>();
	    try {
	      data.addAll((List<?>) retrieveData(
	          uriInfo.getStartEntitySet(),
	          uriInfo.getKeyPredicates(),
	          uriInfo.getFunctionImport(),
	          mapFunctionParameters(uriInfo.getFunctionImportParameters()),
	          uriInfo.getNavigationSegments()));
	    } catch (final ODataNotFoundException e) {
	      data.clear();
	    }

	    final EdmEntitySet entitySet = uriInfo.getTargetEntitySet();
	    final InlineCount inlineCountType = uriInfo.getInlineCount();
	    final Integer count = applySystemQueryOptions(
	        entitySet,
	        data,
	        uriInfo.getFilter(),
	        inlineCountType,
	        uriInfo.getOrderBy(),
	        uriInfo.getSkipToken(),
	        uriInfo.getSkip(),
	        uriInfo.getTop());

	    ODataContext context = getContext();
	    String nextLink = null;

	    // Limit the number of returned entities and provide a "next" link
	    // if there are further entities.
	    // Almost all system query options in the current request must be carried
	    // over to the URI for the "next" link, with the exception of $skiptoken
	    // and $skip.
	    if (data.size() > SERVER_PAGING_SIZE) {
	      if (uriInfo.getOrderBy() == null
	          && uriInfo.getSkipToken() == null
	          && uriInfo.getSkip() == null
	          && uriInfo.getTop() == null) {
	        sortInDefaultOrder(entitySet, data);
	      }

	      nextLink = context.getPathInfo().getServiceRoot().relativize(context.getPathInfo().getRequestUri()).toString();
	      nextLink = percentEncodeNextLink(nextLink);
	      nextLink += (nextLink.contains("?") ? "&" : "?")
	          + "$skiptoken=" + getSkipToken(entitySet, data.get(SERVER_PAGING_SIZE));

	      while (data.size() > SERVER_PAGING_SIZE) {
	        data.remove(SERVER_PAGING_SIZE);
	      }
	    }

	    final EdmEntityType entityType = entitySet.getEntityType();
	    List<Map<String, Object>> values = new ArrayList<Map<String, Object>>();
	    for (final Object entryData : data) {
	      values.add(getStructuralTypeValueMap(entryData, entityType));
	    }

	    final EntityProviderWriteProperties feedProperties = EntityProviderWriteProperties
	        .serviceRoot(context.getPathInfo().getServiceRoot())
	        .inlineCountType(inlineCountType)
	        .inlineCount(count)
	        .expandSelectTree(UriParser.createExpandSelectTree(uriInfo.getSelect(), uriInfo.getExpand()))
	        .callbacks(gettombstoneCallbacks(context, uriInfo))//getCallbacks(data, entityType))
	        .nextLink(nextLink)
	        .build();

	    final int timingHandle = context.startRuntimeMeasurement("EntityProvider", "writeFeed");
	    final ODataResponse response = EntityProvider.writeFeed(contentType, entitySet, values, feedProperties);

	    context.stopRuntimeMeasurement(timingHandle);

	    return ODataResponse.fromResponse(response).build();
	}
	  String percentEncodeNextLink(final String link) {
		    if (link == null) {
		      return null;
		    }

		    return link.replaceAll("\\$skiptoken=.+?(?:&|$)", "")
		        .replaceAll("\\$skip=.+?(?:&|$)", "")
		        .replaceFirst("(?:\\?|&)$", ""); // Remove potentially trailing "?" or "&" left over from remove actions
		  }
	private static Map<String, Object> mapKey(final List<KeyPredicate> keys) throws EdmException {
	    Map<String, Object> keyMap = new HashMap<String, Object>();
	    for (final KeyPredicate key : keys) {
	      final EdmProperty property = key.getProperty();
	      final EdmSimpleType type = (EdmSimpleType) property.getType();
	      keyMap.put(property.getName(), type.valueOfString(key.getLiteral(), EdmLiteralKind.DEFAULT, property.getFacets(),
	          type.getDefaultType()));
	    }
	    return keyMap;
	  }

	  private static Map<String, Object> mapFunctionParameters(final Map<String, EdmLiteral> functionImportParameters)
	      throws EdmSimpleTypeException {
	    if (functionImportParameters == null) {
	      return Collections.emptyMap();
	    } else {
	      Map<String, Object> parameterMap = new HashMap<String, Object>();
	      for (final String parameterName : functionImportParameters.keySet()) {
	        final EdmLiteral literal = functionImportParameters.get(parameterName);
	        final EdmSimpleType type = literal.getType();
	        parameterMap.put(parameterName, type.valueOfString(literal.getLiteral(), EdmLiteralKind.DEFAULT, null, type
	            .getDefaultType()));
	      }
	      return parameterMap;
	    }
	  }

	  private Object retrieveData(final EdmEntitySet startEntitySet, final List<KeyPredicate> keyPredicates,
	      final EdmFunctionImport functionImport, final Map<String, Object> functionImportParameters,
	      final List<NavigationSegment> navigationSegments) throws ODataException {
	    Object data;
	    final Map<String, Object> keys = mapKey(keyPredicates);

	    ODataContext context = getContext();
	    final int timingHandle = context.startRuntimeMeasurement(getClass().getSimpleName(), "retrieveData");

	    try {
	      data = functionImport == null ?
	          keys.isEmpty() ?
	              dataSource.readData(startEntitySet) : dataSource.readData(startEntitySet, keys) :
	          dataSource.readData(functionImport, functionImportParameters, keys);

	      EdmEntitySet currentEntitySet =
	          functionImport == null ? startEntitySet : functionImport.getEntitySet();
	      for (NavigationSegment navigationSegment : navigationSegments) {
	        data = dataSource.readRelatedData(
	            currentEntitySet,
	            data,
	            navigationSegment.getEntitySet(),
	            mapKey(navigationSegment.getKeyPredicates()));
	        currentEntitySet = navigationSegment.getEntitySet();
	      }
	    } finally {
	      context.stopRuntimeMeasurement(timingHandle);
	    }
	    return data;
	  }

	  private <T> Map<String, ODataCallback> getCallbacks(final T data,final EdmEntityType entityType  )
	      throws EdmException {
	    final List<String> navigationPropertyNames = entityType.getNavigationPropertyNames();
	    Map<String, ODataCallback> callbacks = new HashMap<String, ODataCallback>();
	    
	    if (navigationPropertyNames.isEmpty()) {
	      
		  return null;
	      
	    } else {
	      final WriteCallback callback = new WriteCallback(data);
	     // Map<String, ODataCallback> callbacks = new HashMap<String, ODataCallback>();
	      for (final String name : navigationPropertyNames) {
	        callbacks.put(name, callback);
	      }
	      
	      	      
	    }
	    return callbacks;
	  }
	  
	  private <T> Map<String, ODataCallback> gettombstoneCallbacks(final ODataContext context, final GetEntitySetUriInfo uriInfo)
		      throws EdmException {
		   
		    Map<String, ODataCallback> callbacks = new HashMap<String, ODataCallback>();
		    String baseUri = new String("");
		    try {
				baseUri = context.getPathInfo().getServiceRoot().toString();
			} catch (ODataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    TombstoneCallback tombstoneCallback = new  TombstomeCallbackImpl(baseUri,"1234567",uriInfo);
		    callbacks.put(TombstoneCallback.CALLBACK_KEY_TOMBSTONE, tombstoneCallback);
		      
		    
		    return callbacks;
		  }

	  private class WriteCallback implements OnWriteEntryContent, OnWriteFeedContent {
	    private final Object data;

	    private <T> WriteCallback(final T data) {
	      this.data = data;
	    }

	    @Override
	    public WriteFeedCallbackResult retrieveFeedResult(final WriteFeedCallbackContext context)
	        throws ODataApplicationException {
	      try {
	        final EdmEntityType entityType =
	            context.getSourceEntitySet().getRelatedEntitySet(context.getNavigationProperty()).getEntityType();
	        List<Map<String, Object>> values = new ArrayList<Map<String, Object>>();
	        Object relatedData = null;
	        try {
	          relatedData = readRelatedData(context);
	          for (final Object entryData : (List<?>) relatedData) {
	            values.add(getStructuralTypeValueMap(entryData, entityType));
	          }
	        } catch (final ODataNotFoundException e) {
	          values.clear();
	        }
	        WriteFeedCallbackResult result = new WriteFeedCallbackResult();
	        result.setFeedData(values);
	        EntityProviderWriteProperties inlineProperties =
	            EntityProviderWriteProperties.serviceRoot(getContext().getPathInfo().getServiceRoot()).callbacks(
	                getCallbacks(relatedData, entityType)).expandSelectTree(context.getCurrentExpandSelectTreeNode())
	                .selfLink(context.getSelfLink()).build();
	        result.setInlineProperties(inlineProperties);
	        return result;
	      } catch (final ODataException e) {
	        throw new ODataApplicationException(e.getLocalizedMessage(), Locale.ROOT, e);
	      }
	    }

	    @Override
	    public WriteEntryCallbackResult retrieveEntryResult(final WriteEntryCallbackContext context)
	        throws ODataApplicationException {
	      try {
	        final EdmEntityType entityType =
	            context.getSourceEntitySet().getRelatedEntitySet(context.getNavigationProperty()).getEntityType();
	        WriteEntryCallbackResult result = new WriteEntryCallbackResult();
	        Object relatedData;
	        try {
	          relatedData = readRelatedData(context);
	        } catch (final ODataNotFoundException e) {
	          relatedData = null;
	        }

	        if (relatedData == null) {
	          result.setEntryData(Collections.<String, Object> emptyMap());
	        } else {
	          result.setEntryData(getStructuralTypeValueMap(relatedData, entityType));

	          EntityProviderWriteProperties inlineProperties =
	              EntityProviderWriteProperties.serviceRoot(getContext().getPathInfo().getServiceRoot()).callbacks(
	                  getCallbacks(relatedData, entityType)).expandSelectTree(context.getCurrentExpandSelectTreeNode())
	                  .build();
	          result.setInlineProperties(inlineProperties);
	        }
	        return result;
	      } catch (final ODataException e) {
	        throw new ODataApplicationException(e.getLocalizedMessage(), Locale.ROOT, e);
	      }
	    }

	    private Object readRelatedData(final WriteCallbackContext context) throws ODataException {
	      final EdmEntitySet entitySet = context.getSourceEntitySet();
	      return dataSource.readRelatedData(
	          entitySet,
	          data instanceof List ? readEntryData((List<?>) data, entitySet.getEntityType(), context
	              .extractKeyFromEntryData()) : data,
	          entitySet.getRelatedEntitySet(context.getNavigationProperty()),
	          Collections.<String, Object> emptyMap());
	    }

	    private <T> T readEntryData(final List<T> data, final EdmEntityType entityType, final Map<String, Object> key)
	        throws ODataException {
	      for (final T entryData : data) {
	        boolean found = true;
	        for (final EdmProperty keyProperty : entityType.getKeyProperties()) {
	          if (!valueAccess.getPropertyValue(entryData, keyProperty).equals(key.get(keyProperty.getName()))) {
	            found = false;
	            break;
	          }
	        }
	        if (found) {
	          return entryData;
	        }
	      }
	      return null;
	    }
	  }

	  private <T> Integer applySystemQueryOptions(final EdmEntitySet entitySet, final List<T> data,
	      final FilterExpression filter, final InlineCount inlineCount, final OrderByExpression orderBy,
	      final String skipToken, final Integer skip, final Integer top) throws ODataException {
	    ODataContext context = getContext();
	    final int timingHandle = context.startRuntimeMeasurement(getClass().getSimpleName(), "applySystemQueryOptions");

	    if (filter != null) {
	      // Remove all elements the filter does not apply for.
	      // A for-each loop would not work with "remove", see Java documentation.
	      for (Iterator<T> iterator = data.iterator(); iterator.hasNext();) {
	        if (!appliesFilter(iterator.next(), filter)) {
	          iterator.remove();
	        }
	      }
	    }

	    final Integer count = inlineCount == InlineCount.ALLPAGES ? data.size() : null;

	    if (orderBy != null) {
	      sort(data, orderBy);
	    } else if (skipToken != null || skip != null || top != null) {
	      sortInDefaultOrder(entitySet, data);
	    }

	    if (skipToken != null) {
	      while (!data.isEmpty() && !getSkipToken(entitySet, data.get(0)).equals(skipToken)) {
	        data.remove(0);
	      }
	    }

	    if (skip != null) {
	      if (skip >= data.size()) {
	        data.clear();
	      } else {
	        for (int i = 0; i < skip; i++) {
	          data.remove(0);
	        }
	      }
	    }

	    if (top != null) {
	      while (data.size() > top) {
	        data.remove(top.intValue());
	      }
	    }

	    context.stopRuntimeMeasurement(timingHandle);

	    return count;
	  }

	  private <T> void sort(final List<T> data, final OrderByExpression orderBy) {
	    Collections.sort(data, new Comparator<T>() {
	      @Override
	      public int compare(final T entity1, final T entity2) {
	        try {
	          int result = 0;
	          for (final OrderExpression expression : orderBy.getOrders()) {
	            String first = evaluateExpression(entity1, expression.getExpression());
	            String second = evaluateExpression(entity2, expression.getExpression());

	            if (first != null && second != null) {
	              result = first.compareTo(second);
	            } else if (first == null && second != null) {
	              result = 1;
	            } else if (first != null && second == null) {
	              result = -1;
	            }

	            if (expression.getSortOrder() == SortOrder.desc) {
	              result = -result;
	            }

	            if (result != 0) {
	              break;
	            }
	          }
	          return result;
	        } catch (final ODataException e) {
	          return 0;
	        }
	      }
	    });
	  }

	  private <T> void sortInDefaultOrder(final EdmEntitySet entitySet, final List<T> data) {
	    Collections.sort(data, new Comparator<T>() {
	      @Override
	      public int compare(final T entity1, final T entity2) {
	        try {
	          return getSkipToken(entitySet, entity1).compareTo(getSkipToken(entitySet, entity2));
	        } catch (final ODataException e) {
	          return 0;
	        }
	      }
	    });
	  }

	  private <T> boolean appliesFilter(final T data, final FilterExpression filter) throws ODataException {
	    ODataContext context = getContext();
	    final int timingHandle = context.startRuntimeMeasurement(getClass().getSimpleName(), "appliesFilter");

	    try {
	      return data != null && (filter == null || evaluateExpression(data, filter.getExpression()).equals("true"));
	    } catch (final RuntimeException e) {
	      return false;
	    } finally {
	      context.stopRuntimeMeasurement(timingHandle);
	    }
	  }

	  private <T> String evaluateExpression(final T data, final CommonExpression expression) throws ODataException {
	    switch (expression.getKind()) {
	    case UNARY:
	      final UnaryExpression unaryExpression = (UnaryExpression) expression;
	      final String operand = evaluateExpression(data, unaryExpression.getOperand());

	      switch (unaryExpression.getOperator()) {
	      case NOT:
	        return Boolean.toString(!Boolean.parseBoolean(operand));
	      case MINUS:
	        return operand.startsWith("-") ? operand.substring(1) : "-" + operand;
	      default:
	        throw new ODataNotImplementedException();
	      }

	    case BINARY:
	      final BinaryExpression binaryExpression = (BinaryExpression) expression;
	      final EdmSimpleType type = (EdmSimpleType) binaryExpression.getLeftOperand().getEdmType();
	      final String left = evaluateExpression(data, binaryExpression.getLeftOperand());
	      final String right = evaluateExpression(data, binaryExpression.getRightOperand());

	      switch (binaryExpression.getOperator()) {
	      case ADD:
	        if (binaryExpression.getEdmType() == EdmSimpleTypeKind.Decimal.getEdmSimpleTypeInstance()
	            || binaryExpression.getEdmType() == EdmSimpleTypeKind.Double.getEdmSimpleTypeInstance()
	            || binaryExpression.getEdmType() == EdmSimpleTypeKind.Single.getEdmSimpleTypeInstance()) {
	          return Double.toString(Double.valueOf(left) + Double.valueOf(right));
	        } else {
	          return Long.toString(Long.valueOf(left) + Long.valueOf(right));
	        }
	      case SUB:
	        if (binaryExpression.getEdmType() == EdmSimpleTypeKind.Decimal.getEdmSimpleTypeInstance()
	            || binaryExpression.getEdmType() == EdmSimpleTypeKind.Double.getEdmSimpleTypeInstance()
	            || binaryExpression.getEdmType() == EdmSimpleTypeKind.Single.getEdmSimpleTypeInstance()) {
	          return Double.toString(Double.valueOf(left) - Double.valueOf(right));
	        } else {
	          return Long.toString(Long.valueOf(left) - Long.valueOf(right));
	        }
	      case MUL:
	        if (binaryExpression.getEdmType() == EdmSimpleTypeKind.Decimal.getEdmSimpleTypeInstance()
	            || binaryExpression.getEdmType() == EdmSimpleTypeKind.Double.getEdmSimpleTypeInstance()
	            || binaryExpression.getEdmType() == EdmSimpleTypeKind.Single.getEdmSimpleTypeInstance()) {
	          return Double.toString(Double.valueOf(left) * Double.valueOf(right));
	        } else {
	          return Long.toString(Long.valueOf(left) * Long.valueOf(right));
	        }
	      case DIV:
	        final String number = Double.toString(Double.valueOf(left) / Double.valueOf(right));
	        return number.endsWith(".0") ? number.replace(".0", "") : number;
	      case MODULO:
	        if (binaryExpression.getEdmType() == EdmSimpleTypeKind.Decimal.getEdmSimpleTypeInstance()
	            || binaryExpression.getEdmType() == EdmSimpleTypeKind.Double.getEdmSimpleTypeInstance()
	            || binaryExpression.getEdmType() == EdmSimpleTypeKind.Single.getEdmSimpleTypeInstance()) {
	          return Double.toString(Double.valueOf(left) % Double.valueOf(right));
	        } else {
	          return Long.toString(Long.valueOf(left) % Long.valueOf(right));
	        }
	      case AND:
	        return Boolean.toString(left.equals("true") && right.equals("true"));
	      case OR:
	        return Boolean.toString(left.equals("true") || right.equals("true"));
	      case EQ:
	        return Boolean.toString(left.equals(right));
	      case NE:
	        return Boolean.toString(!left.equals(right));
	      case LT:
	        if (type == EdmSimpleTypeKind.String.getEdmSimpleTypeInstance()
	            || type == EdmSimpleTypeKind.DateTime.getEdmSimpleTypeInstance()
	            || type == EdmSimpleTypeKind.DateTimeOffset.getEdmSimpleTypeInstance()
	            || type == EdmSimpleTypeKind.Guid.getEdmSimpleTypeInstance()
	            || type == EdmSimpleTypeKind.Time.getEdmSimpleTypeInstance()) {
	          return Boolean.toString(left.compareTo(right) < 0);
	        } else {
	          return Boolean.toString(Double.valueOf(left) < Double.valueOf(right));
	        }
	      case LE:
	        if (type == EdmSimpleTypeKind.String.getEdmSimpleTypeInstance()
	            || type == EdmSimpleTypeKind.DateTime.getEdmSimpleTypeInstance()
	            || type == EdmSimpleTypeKind.DateTimeOffset.getEdmSimpleTypeInstance()
	            || type == EdmSimpleTypeKind.Guid.getEdmSimpleTypeInstance()
	            || type == EdmSimpleTypeKind.Time.getEdmSimpleTypeInstance()) {
	          return Boolean.toString(left.compareTo(right) <= 0);
	        } else {
	          return Boolean.toString(Double.valueOf(left) <= Double.valueOf(right));
	        }
	      case GT:
	        if (type == EdmSimpleTypeKind.String.getEdmSimpleTypeInstance()
	            || type == EdmSimpleTypeKind.DateTime.getEdmSimpleTypeInstance()
	            || type == EdmSimpleTypeKind.DateTimeOffset.getEdmSimpleTypeInstance()
	            || type == EdmSimpleTypeKind.Guid.getEdmSimpleTypeInstance()
	            || type == EdmSimpleTypeKind.Time.getEdmSimpleTypeInstance()) {
	          return Boolean.toString(left.compareTo(right) > 0);
	        } else {
	          return Boolean.toString(Double.valueOf(left) > Double.valueOf(right));
	        }
	      case GE:
	        if (type == EdmSimpleTypeKind.String.getEdmSimpleTypeInstance()
	            || type == EdmSimpleTypeKind.DateTime.getEdmSimpleTypeInstance()
	            || type == EdmSimpleTypeKind.DateTimeOffset.getEdmSimpleTypeInstance()
	            || type == EdmSimpleTypeKind.Guid.getEdmSimpleTypeInstance()
	            || type == EdmSimpleTypeKind.Time.getEdmSimpleTypeInstance()) {
	          return Boolean.toString(left.compareTo(right) >= 0);
	        } else {
	          return Boolean.toString(Double.valueOf(left) >= Double.valueOf(right));
	        }
	      case PROPERTY_ACCESS:
	        throw new ODataNotImplementedException();
	      default:
	        throw new ODataNotImplementedException();
	      }

	    case PROPERTY:
	      final EdmProperty property = (EdmProperty) ((PropertyExpression) expression).getEdmProperty();
	      final EdmSimpleType propertyType = (EdmSimpleType) property.getType();
	      return propertyType.valueToString(valueAccess.getPropertyValue(data, property), EdmLiteralKind.DEFAULT,
	          property.getFacets());

	    case MEMBER:
	      final MemberExpression memberExpression = (MemberExpression) expression;
	      final PropertyExpression propertyExpression = (PropertyExpression) memberExpression.getProperty();
	      final EdmProperty memberProperty = (EdmProperty) propertyExpression.getEdmProperty();
	      final EdmSimpleType memberType = (EdmSimpleType) memberExpression.getEdmType();
	      List<EdmProperty> propertyPath = new ArrayList<EdmProperty>();
	      CommonExpression currentExpression = memberExpression;
	      while (currentExpression != null) {
	        final PropertyExpression currentPropertyExpression =
	            (PropertyExpression) (currentExpression.getKind() == ExpressionKind.MEMBER ?
	                ((MemberExpression) currentExpression).getProperty() : currentExpression);
	        final EdmTyped currentProperty = currentPropertyExpression.getEdmProperty();
	        final EdmTypeKind kind = currentProperty.getType().getKind();
	        if (kind == EdmTypeKind.SIMPLE || kind == EdmTypeKind.COMPLEX) {
	          propertyPath.add(0, (EdmProperty) currentProperty);
	        } else {
	          throw new ODataNotImplementedException();
	        }
	        currentExpression =
	            currentExpression.getKind() == ExpressionKind.MEMBER ? ((MemberExpression) currentExpression).getPath()
	                : null;
	      }
	      return memberType.valueToString(getPropertyValue(data, propertyPath), EdmLiteralKind.DEFAULT, memberProperty
	          .getFacets());

	    case LITERAL:
	      final LiteralExpression literal = (LiteralExpression) expression;
	      final EdmSimpleType literalType = (EdmSimpleType) literal.getEdmType();
	      return literalType.valueToString(literalType.valueOfString(literal.getUriLiteral(), EdmLiteralKind.URI, null,
	          literalType.getDefaultType()),
	          EdmLiteralKind.DEFAULT, null);

	    case METHOD:
	      final MethodExpression methodExpression = (MethodExpression) expression;
	      final String first = evaluateExpression(data, methodExpression.getParameters().get(0));
	      final String second = methodExpression.getParameterCount() > 1 ?
	          evaluateExpression(data, methodExpression.getParameters().get(1)) : null;
	      final String third = methodExpression.getParameterCount() > 2 ?
	          evaluateExpression(data, methodExpression.getParameters().get(2)) : null;

	      switch (methodExpression.getMethod()) {
	      case ENDSWITH:
	        return Boolean.toString(first.endsWith(second));
	      case INDEXOF:
	        return Integer.toString(first.indexOf(second));
	      case STARTSWITH:
	        return Boolean.toString(first.startsWith(second));
	      case TOLOWER:
	        return first.toLowerCase(Locale.ROOT);
	      case TOUPPER:
	        return first.toUpperCase(Locale.ROOT);
	      case TRIM:
	        return first.trim();
	      case SUBSTRING:
	        final int offset = Integer.parseInt(second);
	        return first.substring(offset, offset + Integer.parseInt(third));
	      case SUBSTRINGOF:
	        return Boolean.toString(second.contains(first));
	      case CONCAT:
	        return first + second;
	      case LENGTH:
	        return Integer.toString(first.length());
	      case YEAR:
	        return String.valueOf(Integer.parseInt(first.substring(0, 4)));
	      case MONTH:
	        return String.valueOf(Integer.parseInt(first.substring(5, 7)));
	      case DAY:
	        return String.valueOf(Integer.parseInt(first.substring(8, 10)));
	      case HOUR:
	        return String.valueOf(Integer.parseInt(first.substring(11, 13)));
	      case MINUTE:
	        return String.valueOf(Integer.parseInt(first.substring(14, 16)));
	      case SECOND:
	        return String.valueOf(Integer.parseInt(first.substring(17, 19)));
	      case ROUND:
	        return Long.toString(Math.round(Double.valueOf(first)));
	      case FLOOR:
	        return Long.toString(Math.round(Math.floor(Double.valueOf(first))));
	      case CEILING:
	        return Long.toString(Math.round(Math.ceil(Double.valueOf(first))));
	      default:
	        throw new ODataNotImplementedException();
	      }

	    default:
	      throw new ODataNotImplementedException();
	    }
	  }

	  private <T> String getSkipToken(final EdmEntitySet entitySet, final T data) throws ODataException {
	    String skipToken = "";
	    for (final EdmProperty property : entitySet.getEntityType().getKeyProperties()) {
	      final EdmSimpleType type = (EdmSimpleType) property.getType();
	      skipToken = skipToken.concat(type.valueToString(valueAccess.getPropertyValue(data, property),
	          EdmLiteralKind.DEFAULT, property.getFacets()));
	    }
	    return skipToken;
	  }

	  private <T> Object getPropertyValue(final T data, final List<EdmProperty> propertyPath) throws ODataException {
	    Object dataObject = data;
	    for (final EdmProperty property : propertyPath) {
	      if (dataObject != null) {
	        dataObject = valueAccess.getPropertyValue(dataObject, property);
	      }
	    }
	    return dataObject;
	  }

	  private void handleMimeType(final Object data, final EdmMapping mapping, final Map<String, Object> valueMap)
	      throws ODataException {
	    final String mimeTypeName = mapping.getMediaResourceMimeTypeKey();
	    if (mimeTypeName != null) {
	      Object value = valueAccess.getMappingValue(data, mapping);
	      valueMap.put(mimeTypeName, value);
	    }
	  }

	  private <T> Map<String, Object> getSimpleTypeValueMap(final T data, final List<EdmProperty> propertyPath)
	      throws ODataException {
	    final EdmProperty property = propertyPath.get(propertyPath.size() - 1);
	    Map<String, Object> valueWithMimeType = new HashMap<String, Object>();
	    valueWithMimeType.put(property.getName(), getPropertyValue(data, propertyPath));

	    handleMimeType(data, property.getMapping(), valueWithMimeType);
	    return valueWithMimeType;
	  }

	  private <T> Map<String, Object> getStructuralTypeValueMap(final T data, final EdmStructuralType type)
	      throws ODataException {
	    ODataContext context = getContext();
	    final int timingHandle = context.startRuntimeMeasurement(getClass().getSimpleName(), "getStructuralTypeValueMap");

	    Map<String, Object> valueMap = new HashMap<String, Object>();

	    EdmMapping mapping = type.getMapping();
	    if (mapping != null) {
	      handleMimeType(data, mapping, valueMap);
	    }

	    for (final String propertyName : type.getPropertyNames()) {
	      final EdmProperty property = (EdmProperty) type.getProperty(propertyName);
	      final Object value = valueAccess.getPropertyValue(data, property);

	      if (property.isSimple()) {
	        if (property.getMapping() == null || property.getMapping().getMediaResourceMimeTypeKey() == null) {
	          valueMap.put(propertyName, value);
	        } else {
	          // TODO: enable MIME type mapping outside the current subtree
	          valueMap.put(propertyName, getSimpleTypeValueMap(data, Arrays.asList(property)));
	        }
	      } else {
	        valueMap.put(propertyName, getStructuralTypeValueMap(value, (EdmStructuralType) property.getType()));
	      }
	    }

	    context.stopRuntimeMeasurement(timingHandle);

	    return valueMap;
	  }

	  @Override
	  public ODataResponse executeBatch(final BatchHandler handler, final String contentType, final InputStream content)
	      throws ODataException {
	    ODataResponse batchResponse;
	    List<BatchResponsePart> batchResponseParts = new ArrayList<BatchResponsePart>();
	    PathInfo pathInfo = getContext().getPathInfo();
	    EntityProviderBatchProperties batchProperties = EntityProviderBatchProperties.init().pathInfo(pathInfo).build();
	    List<BatchRequestPart> batchParts = EntityProvider.parseBatchRequest(contentType, content, batchProperties);
	    for (BatchRequestPart batchPart : batchParts) {
	      batchResponseParts.add(handler.handleBatchPart(batchPart));
	    }
	    batchResponse = EntityProvider.writeBatchResponse(batchResponseParts);
	    return batchResponse;
	  }

	  @Override
	  public BatchResponsePart executeChangeSet(final BatchHandler handler, final List<ODataRequest> requests)
	      throws ODataException {
	    List<ODataResponse> responses = new ArrayList<ODataResponse>();
	    for (ODataRequest request : requests) {
	      ODataResponse response = handler.handleRequest(request);
	      if (response.getStatus().getStatusCode() >= HttpStatusCodes.BAD_REQUEST.getStatusCode()) {
	        // Rollback
	        List<ODataResponse> errorResponses = new ArrayList<ODataResponse>(1);
	        errorResponses.add(response);
	        return BatchResponsePart.responses(errorResponses).changeSet(false).build();
	      }
	      responses.add(response);
	    }
	    return BatchResponsePart.responses(responses).changeSet(true).build();
	  }
	

}


