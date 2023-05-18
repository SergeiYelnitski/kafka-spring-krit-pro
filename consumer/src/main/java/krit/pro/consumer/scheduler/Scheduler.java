//package krit.pro.consumer.scheduler;
//
//import com.fandispatch.fandispatch.models.RoundScheduler;
//import com.fandispatch.fandispatch.models.meta.*;
//import com.fandispatch.fandispatch.services.*;
//import com.fandispatch.fandispatch.websocket.MessageEvent;
//import com.fandispatch.fandispatch.websocket.NewDeliveryMessenger;
//import com.fandispatch.fandispatch.websocket.NewImportContacts;
//import com.fandispatch.fandispatch.websocket.StartPageMessage;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.env.Environment;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.lang.reflect.InvocationTargetException;
//import java.text.ParseException;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//@Component
//@Slf4j
//public class Scheduler {
//
//    @Autowired
//    CommonServiceDao commonServiceDao;
//
//    @Autowired
//    DeliveryGeneratingService deliveryGeneratingService;
//
//    @Autowired
//    MessagesGeneratingService messagesGeneratingService;
//
//    @Autowired
//    CheckStatusService checkStatusService;
//    @Autowired
//    CheckDeliveryStatusService checkDeliveryStatusService;
//    @Autowired
//    SendMessageService sendMessageService;
//    @Autowired
//    DeliveryFinalizationService deliveryFinalizationService;
//
//    @Autowired
//    NewDeliveryMessenger messenger;
//
//    @Autowired
//    NewImportContacts sideBarMessenger;
//
//    @Autowired
//    DeliveryForStartPageConverterService converter;
//
//    @Autowired
//    Environment environment;
//
//    private long idxThread = 1;
//    //    @Value( "${scheduler.debug}" )
//    private boolean debug = false;
//    private boolean process = false;
//    private boolean initialized = false;
//
//    private void init(){
//        if (initialized) return;
//        debug = Boolean.parseBoolean(environment.getProperty("scheduler.debug"));
//        process = Boolean.parseBoolean(environment.getProperty("scheduler.process"));
//        initialized = true;
//    }
//
//    public void sendWebSocketMessage(){
//        StartPageMessage message = new StartPageMessage(MessageEvent.FINAL_UPDATE,null);
//        //log.error("SOCKET SEND START" + LocalDateTime.now());
//        messenger.sendMessage(message);
//        //log.error("SOCKET SEND FINISH" + LocalDateTime.now());
//    }
//    public void sendInfoAboutImport(Boolean isImported){
//        sideBarMessenger.sendMessage(isImported);
//    }
//    //fixedRate in milliseconds
//    @Scheduled(fixedDelay = 1000)
//    public void reportCurrentData() throws NoSuchFieldException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, ParseException {
//        init();
//        if (!process) return;
////        idxThread++;
////        if (debug)
////            System.out.println("Scheduler read: " + new Date());
////        new Thread(new SchedulerRunnable(idxThread)).start();
//
//        List<RoundScheduler> entity = new ArrayList();
//
//        RegisterFilter factStartIsNull = new RegisterFilter("factStart",null, TypeComparison.isNull);
//        RegisterFilter planStartLessThanOrEqualNow = new RegisterFilter("planStart", LocalDateTime.now(), TypeComparison.LessThanOrEqual);
//        RegisterFilter schedulerNotPeriodly = new RegisterFilter("schedulerPeriods",25, TypeComparison.Equal);
//        List<RegisterFilter> filterList = new ArrayList<>();
//
//        RegisterFilters registerFilters = new RegisterFilters();
//
//        registerFilters.setCondition(FilterCondition.And);
//        registerFilters.setCaseSensitive(false);
//        List<SortedField> sortedFieldList = new ArrayList<>();
//        SortedField sortByPlan = new SortedField();
//        sortByPlan.setName("planStart");
//        sortByPlan.setOrder(SortedOrder.Asc.ordType());
//        sortedFieldList.add(sortByPlan);
//
//        filterList.add(planStartLessThanOrEqualNow);
//        filterList.add(factStartIsNull);
//        filterList.add(schedulerNotPeriodly);
//        registerFilters.setFilters(filterList);
//
//        Map<String,Object> isImported = commonServiceDao.GetInfoAboutImport();
//        sendInfoAboutImport((Boolean) isImported.get("isImported"));
//        entity = commonServiceDao.findEntity(RoundScheduler.class,
//                registerFilters,
//                null,
//                null, 1,
//                sortedFieldList);
//        // log.error("Найдено элементов для планировщика и отсортировано: "+entity.size());
//
//        //  messenger.sendMessage(message);
//
//        if (entity.stream().count() == 0) {
//            RegisterFilter schedulerPeriodly = new RegisterFilter("schedulerPeriods",25, TypeComparison.NotEqual);
//            filterList.clear();
//            filterList.add(schedulerPeriodly);
//            filterList.add(planStartLessThanOrEqualNow);
//            registerFilters.setFilters(filterList);
//            entity = commonServiceDao.findEntity(RoundScheduler.class,
//                    registerFilters,
//                    null,
//                    null, 1,
//                    sortedFieldList);
//        }
//        //log.error("Найдено элементов для планировщика: "+entity.size());
//        //log.error("Найдено элементов для планировщика: "+entity.stream().map(x->x.getName()+x.getPlanStart().toString())
//        //      .collect(Collectors.toList()).toString());
//
//        int period;
//
//        for(RoundScheduler roundScheduler:entity) {
//            //sendWebSocketMessage();
//            if (debug) {
//                if (roundScheduler.getIdTask() != null)
//                    System.out.println("Schedule_TaskId: " + roundScheduler.getIdTask().getTaskId());
//            }
//
//            period = roundScheduler.getSchedulerPeriods().getKey();
//            switch (period) {
//                case (17): // ежедневно
//                    roundScheduler.setPlanStart(LocalDateTime.now().plusDays(1));
//                    break;
//                case (18): // еженедельно
//                    roundScheduler.setPlanStart(LocalDateTime.now().plusWeeks(1));
//                    break;
//                case (19): // ежечасно
//                    roundScheduler.setPlanStart(LocalDateTime.now().plusHours(1));
//                    break;
//                case (20): // ежеминутно
//                    roundScheduler.setPlanStart(LocalDateTime.now().plusMinutes(1));
//                    break;
//                case (25): // нет периода
////                    roundScheduler.setPlanStart(null);
//                    break;
//                default:break;
//            }
//            roundScheduler.setFactStart(LocalDateTime.now());
//
//            commonServiceDao.updateEntity(roundScheduler);
//            //log.error(String.valueOf(roundScheduler.getIdTask()));
//
//            if (roundScheduler.getIdTask() != null) {
//                switch (roundScheduler.getIdTask().getKey()) {
//                    case (11): //generatedDispatches
//                        // log.debug("Генерация рассылок началась");
//                        if (debug) {
//                            System.out.println("Schedule START: DeliveryGeneratingService.checkDispatches");
//                        }
//                        deliveryGeneratingService.checkDispatches();
//                        //  log.debug("Генерация рассылок завершена");
//
//                        break;
//                    case (12): //generatedLetters
//                        if (debug) {
//                            System.out.println("Schedule START: ");
//                        }
//                        // messagesGeneratingService.generateRoundSchedulerItem();
//                        break;
//                    case (14): //sendedLetters3
//                        if (debug) {
//                            System.out.println("Schedule START: ");
//                        }
//                        break;
//                    case (15): //sendedLetters4
//                        if (debug) {
//                            System.out.println("Schedule START: ");
//                            log.debug("Scheduler debug: entering SendMessageService with Email process id: " + roundScheduler.getIdProcess());
//                        }
//                        sendMessageService.sendMessages(roundScheduler);
//                        sendWebSocketMessage();
//                        break;
//                    case (16): //sendedLetters5
//                        if (debug) {
//                            System.out.println("Schedule START: ");
//                            log.debug("Scheduler debug: entering SendMessageService with SMS process id: " + roundScheduler.getIdProcess());
//                        }
//                        sendMessageService.sendMessages(roundScheduler);
//                        //  message = new StartPageMessage(MessageEvent.UPDATE_TABLE,null);
//                        sendWebSocketMessage();
//                        break;
//                    case (17): //sendedLetters6
//                        if (debug) {
//                            System.out.println("Schedule START: ");
//                        }
//                        break;
//                    case (18): //sendedLetters7
//                        if (debug) {
//                            System.out.println("Schedule START: ");
//                        }
//                        break;
//                    case (19): //sendedLetters9
//                        if (debug) {
//                            System.out.println("Schedule START: ");
//                            log.debug("Scheduler debug: entering SendMessageService with VipNet process id: " + roundScheduler.getIdProcess());
//                        }
//                        sendMessageService.sendMessages(roundScheduler);
//                        // message = new StartPageMessage(MessageEvent.UPDATE_TABLE,null);
//                        sendWebSocketMessage();
//                        break;
//                    case (21): //getStatusLetters4
//                    case (22): //getStatusLetters5
//                    case (23):
//                    case (24): //getStatusLetters6
//                    case (25): //getStatusLetters7
//                    case (26): //getStatusLetters9
//
//                        if (debug) {
//                            System.out.println("Schedule START: ");
//                            log.debug("Scheduler debug: entering CheckStatusService with process id: " + roundScheduler.getIdProcess());
//                        }
//                        checkStatusService.checkStatusByUid(roundScheduler);
//                        break;
//                    case (27): //checkTotalDispatch
//                        if (debug) {
//                            System.out.println("Schedule START: ");
//                            log.debug("Scheduler debug: entering DeliveryFinalizationService with process id: " + roundScheduler.getIdProcess());
//                        }
//                        //log.error("Вошли в подведение итогов");
//                        deliveryFinalizationService.checkTotalDispatch(roundScheduler);
//                        //StartPageMessage message = new StartPageMessage(MessageEvent.FINAL_UPDATE,null);
//                        sendWebSocketMessage();
//                        //log.error("Вышли");
//
//                        break;
//                    case (28): //getStatusDeliveryLetters3
//                    case (29): //getStatusDeliveryLetters4
//                    case (30): //getStatusDeliveryLetters5
//                    case (31): //getStatusDeliveryLetters6
//                    case (32): //getStatusDeliveryLetters7
//                    case (33): //getStatusDeliveryLetters9
//
//                        if (debug) {
//                            System.out.println("Schedule START: ");
//                            log.debug("Scheduler debug: entering CheckDeliveryStatusService with process id: " + roundScheduler.getIdProcess());
//                        }
//                        checkDeliveryStatusService.checkDeliveryStatusByUid(roundScheduler);
//                        break;
//                    default:
//                        break;
//
//                }
//            }
//        }
//        //log.error("Конец прохода планировщика");
//        //log.error("------------------------------------------");
//
//
//    }
//
//}
